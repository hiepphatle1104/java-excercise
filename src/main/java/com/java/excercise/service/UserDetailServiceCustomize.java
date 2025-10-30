package com.java.excercise.service;

import com.java.excercise.model.User;
import com.java.excercise.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserDetailServiceCustomize implements UserDetailsService {
    private final UserRepository userRepo;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> result = userRepo.findByEmail(email);
        if (result.isEmpty())
            throw new UsernameNotFoundException("email not found");

        return result.get();
    }
}
