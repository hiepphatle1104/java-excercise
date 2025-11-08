package com.java.excercise.service.auth;

import com.java.excercise.dto.auth.SignUpRequest;
import com.java.excercise.dto.auth.SignUpResponse;
import com.java.excercise.dto.auth.UserResponse;
import com.java.excercise.exception.EmailAlreadyExistsException;
import com.java.excercise.exception.NotFoundException;
import com.java.excercise.model.entities.User;
import com.java.excercise.model.enums.UserRole;
import com.java.excercise.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
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

    // Get user
    public UserResponse getUserDetail(String id) {
        User user = userRepo.findById(id)
            .orElseThrow(() -> new RuntimeException("User not found"));

        return UserResponse.builder()
            .id(user.getId())
            .name(user.getName())
            .email(user.getEmail())
            .roles(user.getRoles())
            .build();

    }

    public User getUserById(String id) {
        Optional<User> result = userRepo.findById(id);
        if (result.isEmpty())
            throw new NotFoundException("user not found", "USER_NOT_FOUND");

        return result.get();
    }
}
