package com.java.excercise.config;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtDecoderConfig implements JwtDecoder {

    // SRC KEY
    @Value("${spring.app.scrKey}")
    private String secretKey;
    private NimbusJwtDecoder nimbusJwtDecoder;

    // KHỞI TẠO JWTDECODER
    @PostConstruct
    public void init() {
        // NOTES: Xem coi chuyen sang HS256 cho vui
        SecretKey secretKeySpec = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), "HS512");
        nimbusJwtDecoder = NimbusJwtDecoder
            .withSecretKey(secretKeySpec)
            .macAlgorithm(MacAlgorithm.HS512)
            .build();
    }

    @Override
    public Jwt decode(String token) {
        return nimbusJwtDecoder.decode(token);
    }
}
