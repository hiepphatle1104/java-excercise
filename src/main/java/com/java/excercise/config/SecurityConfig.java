package com.java.excercise.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration // 1. Đánh dấu đây là một lớp cấu hình
@EnableWebSecurity
@EnableMethodSecurity // phân quyền trên method
@RequiredArgsConstructor
public class SecurityConfig {
    // DECODER
    private final JwtDecoderConfig jwtDecoderConfig;
    // ENTRYPOINT
    private final CustomEntryPoint customEntryPoint;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // NOTES: Maybe loi cors
            .cors(Customizer.withDefaults())
            .csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(authorize -> authorize
                .requestMatchers(HttpMethod.POST, "/api/auth/signin").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/auth/signout").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/auth/signup").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/auth/refresh").permitAll()
                .requestMatchers("/api/test").permitAll()
                .anyRequest().authenticated()
            )
            // VERIFY TOKEN
            .oauth2ResourceServer((oauth2) -> oauth2
                .jwt(jwtConfigurer -> jwtConfigurer.decoder(jwtDecoderConfig))
                .authenticationEntryPoint(customEntryPoint)
            );
        return http.build();
    }
}
