package com.swappie.service;

import com.swappie.dto.GeneratedToken;
import com.swappie.dto.request.LoginRequest;
import com.swappie.dto.request.LogoutRequest;
import com.swappie.dto.response.LoginResponse;
import com.swappie.exception.ApiError;
import com.swappie.model.RedisToken;
import com.swappie.model.User;
import com.swappie.repository.RedisTokenRepository;
import com.swappie.repository.UserRepository;
import com.nimbusds.jose.JOSEException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

import java.text.ParseException;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final RedisTokenRepository redisTokenRepository;
    private final UserRepository userRepository;

    public LoginResponse login(LoginRequest loginRequest) {
        Authentication authenticate;
        try {
            // đóng gói username và password
            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                    loginRequest.getEmail(), loginRequest.getPassword());

            // nếu login không thành công nó sẽ throw error ở đây luôn mà không chạy xuống dưới
            authenticate = authenticationManager.authenticate(token);
        } catch (AuthenticationException e) {
            throw new ApiError("Invalid email or password", HttpStatus.UNAUTHORIZED, "INVALID_CREDENTIALS");
        }

        // nếu login thành công thì trả về token
        // lấy đối tượng user sau khi authenticated
        User user = (User) authenticate.getPrincipal();
        GeneratedToken accessToken = jwtService.generateAccessToken(user);
        GeneratedToken refreshToken = jwtService.generateRefreshToken(user);

        // LƯU REFRESH TOKEN VÀO REDIS ĐỂ QUẢN LÝ
        RedisToken redisToken = RedisToken.builder()
                .jwtID(refreshToken.getJwtPayload().getJwtID())
                .userID(refreshToken.getJwtPayload().getUserID())
                .timeToLive(refreshToken.getJwtPayload().getExpirationTime().getTime() -
                        refreshToken.getJwtPayload().getIssueTime().getTime())
                .build();
        redisTokenRepository.save(redisToken);

        return LoginResponse.builder()
                .accessToken(accessToken.getToken())
                .refreshToken(refreshToken.getToken())
                .build();
    }

    // Ở đây ta áp dụng Whitelist để quản lý refresh token
    // đánh đổi là nếu người dùng logout mà access token vẫn còn hạn thì token đó vẫn sẽ còn hiệu lực nhưng sau đó thì
    // không thể tạo ra access token mới vì ta đã vô hiệu hóa refresh token hiện tại của người dùng đó rồi
    public void Logout(LogoutRequest logoutRequest) throws ParseException, JOSEException {
        // verify token
        // không cần check expiration time vì khi người dùng logout thì họ chả quan tâm nó còn hạn hay không đâu
        jwtService.verifyToken(logoutRequest.getRefreshToken());

        String refreshTokenID = jwtService.parseToken(logoutRequest.getRefreshToken()).getJwtID();
        if (!redisTokenRepository.existsById(refreshTokenID)) {
            throw new ApiError("User was logout", HttpStatus.BAD_REQUEST, "BAD_REQUEST");
        }
        redisTokenRepository.deleteById(refreshTokenID);
    }

}
