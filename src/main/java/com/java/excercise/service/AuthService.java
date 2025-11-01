package com.java.excercise.service;

import com.java.excercise.dto.GeneratedToken;
import com.java.excercise.dto.TokenList;
import com.java.excercise.dto.request.SignInRequest;
import com.java.excercise.exception.InvalidCredentialsException;
import com.java.excercise.exception.InvalidTokenException;
import com.java.excercise.model.RedisToken;
import com.java.excercise.model.User;
import com.java.excercise.repository.RedisTokenRepository;
import com.nimbusds.jose.JOSEException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

import java.text.ParseException;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final RedisTokenRepository redis;

    public TokenList login(SignInRequest req) {
        Authentication authenticate;
        try {
            // đóng gói username và password
            UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(req.email(), req.password());

            // nếu login không thành công nó sẽ throw error ở đây luôn mà không chạy xuống dưới
            authenticate = authenticationManager.authenticate(auth);
        } catch (AuthenticationException e) {
            throw new InvalidCredentialsException();
        }

        // NOTES: O day khong biet co can access token khong
        // nếu login thành công thì trả về token
        // lấy đối tượng user sau khi authenticated
        User user = (User) authenticate.getPrincipal();
        GeneratedToken accessToken = jwtService.generateToken(user, JwtService.ACCESS_TOKEN_TTL);
        GeneratedToken refreshToken = jwtService.generateToken(user, JwtService.REFRESH_TOKEN_TTL);

        // LƯU REFRESH TOKEN VÀO REDIS ĐỂ QUẢN LÝ
        RedisToken redisToken = RedisToken.map(refreshToken.getJwtPayload());
        redis.save(redisToken);

        return new TokenList(refreshToken.getToken(), accessToken.getToken());
    }

    // Ở đây ta áp dụng Whitelist để quản lý refresh token
    // đánh đổi là nếu người dùng logout mà access token vẫn còn hạn thì token đó vẫn sẽ còn hiệu lực nhưng sau đó thì
    // không thể tạo ra access token mới vì ta đã vô hiệu hóa refresh token hiện tại của người dùng đó rồi
    public void logout(String refreshToken) throws ParseException, JOSEException {
        if (refreshToken == null || refreshToken.isEmpty())
            throw new InvalidTokenException();

        // verify token
        // không cần check expiration time vì khi người dùng logout thì họ chả quan tâm nó còn hạn hay không đâu
        jwtService.verifyToken(refreshToken);

        String tokenId = jwtService.parseToken(refreshToken).getJwtID();
        if (!redis.existsById(tokenId))
            throw new InvalidTokenException();

        redis.deleteById(tokenId);
    }
}
