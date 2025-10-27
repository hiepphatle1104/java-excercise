package backend.project.com.backendproject.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import backend.project.com.backendproject.dto.request.UserRequest;
import backend.project.com.backendproject.dto.response.UserResponse;
import backend.project.com.backendproject.enums.Role;
import backend.project.com.backendproject.exception.ApiError;
import backend.project.com.backendproject.model.User;
import backend.project.com.backendproject.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public UserResponse createUser(UserRequest userRequest) {
        if (userRepository.existsByEmail(userRequest.getEmail()))
            throw new ApiError("User already exists", HttpStatus.BAD_REQUEST, "USER_ALREADY_EXISTS");

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();


        Set<String> roles = new HashSet<String>();
        if (userRequest.getEmail().contains("admin")) {
            roles.add(Role.ADMIN.toString());
        } else {
            roles.add(Role.USER.toString());
        }

        User user = User.builder()
                .email(userRequest.getEmail())
                .name(userRequest.getName())
                .password(passwordEncoder.encode(userRequest.getPassword()))
                .roles(roles)
                .build();

        userRepository.save(user);

        return UserResponse.builder()
                .name(user.getName())
                .email(user.getEmail())
                .roles(user.getRoles())
                .build();
    }


}
