package com.java.excercise.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
public class CorsConfig {
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
