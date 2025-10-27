package com.swappie.configuration;

import com.swappie.service.UserDetailServiceCustomize;
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
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(HttpMethod.POST, "/v1/auth/login").permitAll()
                        .requestMatchers(HttpMethod.POST, "/v1/auth/logout").permitAll()
                        .requestMatchers(HttpMethod.POST, "/v1/register").permitAll()
                        .requestMatchers(HttpMethod.POST, "/v1/auth/createToken").permitAll()
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
    public AuthenticationManager authenticationManager() {

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

}
