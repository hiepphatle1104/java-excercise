package com.java.excercise.configuration;

import com.java.excercise.service.UserDetailServiceCustomize;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration // 1. Đánh dấu đây là một lớp cấu hình
@EnableWebSecurity
@EnableMethodSecurity // phân quyền trên method
@RequiredArgsConstructor
public class SecurityConfiguration {

    // USER DETAIL SERVICE
    private final UserDetailServiceCustomize userDetailServiceCustomize;
    // DECODER
    private final JwtDecoderConfiguration jwtDecoderConfiguration;
    // ENTRYPOINT
    private final CustomAuthenticationEntryPoint authenticationEntryPoint;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // THÊM CẤU HÌNH CORS
                .cors(corsConfigurer -> corsConfigurer
                        .configurationSource(corsConfigurationSource()))
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(HttpMethod.POST, "/api/auth/signin").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/auth/signout").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/auth/signup").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/auth/createToken").permitAll()
                        .requestMatchers("/api/test").permitAll()
                        .anyRequest().authenticated()
                )
                // VERIFY TOKEN
                .oauth2ResourceServer((oauth2) -> oauth2
                        .jwt(jwtConfigurer -> jwtConfigurer.decoder(jwtDecoderConfiguration))
                        .authenticationEntryPoint(authenticationEntryPoint)
                );
        return http.build();
    }


    // Authenticate
    @Bean
    public AuthenticationManager authenticationManager
    () {

        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();

        daoAuthenticationProvider.setUserDetailsService(userDetailServiceCustomize);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());

        return new ProviderManager(daoAuthenticationProvider);
    }


    // mình sử dụng Bcryp để giải mã mk
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // BEAN CẤU HÌNH CORS
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // QUAN TRỌNG: Thay đổi danh sách này bằng origin của frontend (ví dụ: "http://localhost:3000" cho React)
        configuration.setAllowedOrigins(List.of(
                "http://localhost:5173", // Vite
                "http://localhost:8080"  // Thêm các origin khác nếu cần
        ));

        // Cho phép các method này
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));

        // Cho phép các header này
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type", "Cache-Control", "X-Requested-With"));

        // Cho phép gửi cookie
        configuration.setAllowCredentials(true);

        // Thời gian cache cho pre-flight request (OPTIONS)
        configuration.setMaxAge(3600L); // 1 giờ

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        // Áp dụng cấu hình này cho tất cả các đường dẫn
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

}
