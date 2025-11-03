package com.java.excercise.service.auth;

import com.java.excercise.dto.auth.GeneratedToken;
import com.java.excercise.dto.auth.JwtPayload;
import com.java.excercise.dto.auth.TokenList;
import com.java.excercise.exception.ApiError;
import com.java.excercise.exception.InvalidTokenException;
import com.java.excercise.exception.UserNotFoundException;
import com.java.excercise.model.entities.RedisToken;
import com.java.excercise.model.entities.User;
import com.java.excercise.repository.RedisTokenRepository;
import com.java.excercise.repository.UserRepository;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class JwtService {

    //  ( using seconds )
    public static final long ACCESS_TOKEN_TTL = 15 * 60; // 15 minutes
    public static final long REFRESH_TOKEN_TTL = 14 * 24 * 60 * 60; // 14 days

    private final RedisTokenRepository redisTokenRepository;
    private final UserRepository userRepository;

    // SRC KEY
    @Value("${spring.app.scrKey}")
    private String srcKey;

    // TỐI ƯU HÓA
    // Tạo Verifier (và Signer) một lần khi khởi động
    // Thay vì new() mỗi lần, tốn hiệu năng

    private MACSigner macSigner;
    private MACVerifier macVerifier;

    @PostConstruct
    public void init() {
        byte[] secretBytes = srcKey.getBytes(StandardCharsets.UTF_8);
        try {
            this.macSigner = new MACSigner(secretBytes);
            this.macVerifier = new MACVerifier(secretBytes);
        } catch (JOSEException e) {
            throw new RuntimeException(e);
        }
    }

    public GeneratedToken generateToken(User user, long duration) {
        // HEADER
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);

        // PAYLOAD
        Date issueTime = new Date();
        Date expirationTime = Date.from(issueTime.toInstant().plus(duration, ChronoUnit.SECONDS));
        String jwtID = UUID.randomUUID().toString();

        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
            .subject(user.getId())
            .issueTime(issueTime)
            .expirationTime(expirationTime)
            .jwtID(jwtID)
            .claim("scope", buildScope(user))
            .build();

        Payload payload = new Payload(claimsSet.toJSONObject());

        // SIGNATURE
        JWSObject jwsObject = new JWSObject(header, payload);
        try {
            jwsObject.sign(this.macSigner); // dùng signer đã tạo
        } catch (JOSEException e) {
            throw new RuntimeException(e);
        }

        // TẠO TOKEN
        String serialize = jwsObject.serialize();
        JwtPayload resJwtPayload = JwtPayload.builder()
            .userID(user.getId())
            .jwtID(jwtID)
            .roles(user.getRoles())
            .issueTime(issueTime)
            .expirationTime(expirationTime)
            .build();

        return GeneratedToken.builder()
            .token(serialize)
            .jwtPayload(resJwtPayload)
            .build();
    }

    private String buildScope(User user) {
        StringJoiner stringJoiner = new StringJoiner(" ");
        if (!CollectionUtils.isEmpty(user.getRoles()))
            user.getRoles().forEach(stringJoiner::add);

        return stringJoiner.toString();
    }

    public void checkExpirationTime(String token) throws ParseException, JOSEException {
        SignedJWT signedJWT = SignedJWT.parse(token);

        if (signedJWT.getJWTClaimsSet().getExpirationTime().before(new Date())) {
            throw new ApiError("Token is expired", HttpStatus.UNAUTHORIZED, "REFRESH_EXPIRED");
        }
    }

    public void verifyToken(String token) throws ParseException, JOSEException {
        SignedJWT signedJWT = SignedJWT.parse(token);

        if (!signedJWT.verify(this.macVerifier)) {
            throw new ApiError("RefreshToken is invalid", HttpStatus.UNAUTHORIZED, "REFRESH_INVALID");
        }
    }

    public JwtPayload parseToken(String Token) throws ParseException, JOSEException {
        SignedJWT signedJWT = SignedJWT.parse(Token);

        String userId = signedJWT.getJWTClaimsSet().getSubject();
        String jwtID = signedJWT.getJWTClaimsSet().getJWTID();

        // lấy sopce dưới dạng string
        String scope = signedJWT.getJWTClaimsSet().getStringClaim("scope");
        // tách nó ra và lưu vào set
        Set<String> roles = Arrays.stream(scope.split(" "))
            .filter(s -> !s.isEmpty())
            .collect(Collectors.toSet());

        Date issueTime = signedJWT.getJWTClaimsSet().getIssueTime();
        Date expirationTime = signedJWT.getJWTClaimsSet().getExpirationTime();
        return JwtPayload.builder()
            .userID(userId)
            .jwtID(jwtID)
            .roles(roles)
            .issueTime(issueTime)
            .expirationTime(expirationTime)
            .build();
    }

    public TokenList createNewToken(String refreshToken) throws ParseException, JOSEException {

        // kiểm tra xem RT có null hay empty không
        if (refreshToken == null || refreshToken.isEmpty())
            throw new InvalidTokenException();

        // lấy thông tin trong token ra
        JwtPayload jwtPayload = parseToken(refreshToken);
        String jwtID = jwtPayload.getJwtID();
        String userID = jwtPayload.getUserID();

        // verify tokenprducts
        verifyToken(refreshToken);
        checkExpirationTime(refreshToken);

        // check xem có tồn tại token này trong redis không
        // rơi vào case này thì có thể là user đã logout, đổi pass hoặc là đã từng yêu cầu câp token mới rồi
        // khi này bắt user login lại
        if (!redisTokenRepository.existsById(jwtID))
            throw new InvalidTokenException();

        // lấy user trong db ra
        User user = userRepository.findUserById(userID);
        if (user == null)
            throw new UserNotFoundException();

        // tạo token mới
        GeneratedToken newAccessToken = generateToken(user, ACCESS_TOKEN_TTL);
        GeneratedToken newRefreshToken = generateToken(user, REFRESH_TOKEN_TTL);

        // xóa token cũ và thêm token mới vào redis
        redisTokenRepository.deleteById(jwtID);
        var newToken = RedisToken.map(newRefreshToken.getJwtPayload());

        redisTokenRepository.save(newToken);
        return new TokenList(newRefreshToken.getToken(), newAccessToken.getToken());
    }


}
