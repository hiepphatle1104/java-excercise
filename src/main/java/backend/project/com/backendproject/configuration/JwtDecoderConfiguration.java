package backend.project.com.backendproject.configuration;

import backend.project.com.backendproject.service.JwtService;
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
public class JwtDecoderConfiguration implements JwtDecoder {

    // SRC KEY
    @Value("${spring.app.scrKey}")
    private String srcKey;

    private NimbusJwtDecoder nimbusJwtDecoder;
    private final JwtService jwtService;

    // KHỞI TẠO JWTDECODER
    @PostConstruct
    public void init() {
        SecretKey secretKeySpec = new SecretKeySpec(srcKey.getBytes(StandardCharsets.UTF_8), "HS512");
        nimbusJwtDecoder = NimbusJwtDecoder.withSecretKey(secretKeySpec)
                .macAlgorithm(MacAlgorithm.HS512)
                .build();
    }

    @Override
    public Jwt decode(String token) {
        Jwt jwt = nimbusJwtDecoder.decode(token);
        return jwt;
    }

}
