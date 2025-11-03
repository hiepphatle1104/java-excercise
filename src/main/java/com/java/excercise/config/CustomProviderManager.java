package com.java.excercise.config;

import com.java.excercise.service.auth.UserDetailServiceCustomize;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class CustomProviderManager {
    private final PasswordEncoder passwordEncoder;
    private final UserDetailServiceCustomize userDetailService;

    @Bean
    public AuthenticationManager authenticationManager() {
        // NOTES: Provider kiểu này khá cũ nên fix
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();

        daoAuthenticationProvider.setUserDetailsService(userDetailService);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder);

        return new ProviderManager(daoAuthenticationProvider);
    }
}
