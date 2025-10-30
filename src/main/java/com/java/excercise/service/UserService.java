package com.java.excercise.service;

import com.java.excercise.dto.request.SignUpRequest;
import com.java.excercise.dto.response.SignUpResponse;
import com.java.excercise.exception.EmailAlreadyExistsException;
import com.java.excercise.model.User;
import com.java.excercise.model.enums.UserRole;
import com.java.excercise.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;

    public SignUpResponse createUser(SignUpRequest req) {
        if (userRepo.existsByEmail(req.email()))
            throw new EmailAlreadyExistsException();

        // TODO: Need to set default role into USER
        Set<String> roles = new HashSet<>();

        // TODO: Need to delete hardcode
        if (req.email().equals("admin"))
            roles.add(UserRole.ADMIN.toString());
        else
            roles.add(UserRole.USER.toString());

        User user = User.map(req, roles);
        user.setPassword(passwordEncoder.encode(req.password()));

        return SignUpResponse.map(userRepo.save(user));
    }
}
