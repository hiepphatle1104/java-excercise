package com.swappie.authservice.controller;

import com.swappie.authservice.domain.Session;
import com.swappie.authservice.domain.User;
import com.swappie.authservice.dto.SessionResponseDTO;
import com.swappie.authservice.dto.UserCreationRequest;
import com.swappie.authservice.dto.UserLoginRequest;
import com.swappie.authservice.dto.UserResponseDTO;
import com.swappie.authservice.service.SessionService;
import com.swappie.authservice.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final SessionService sessionService;

    @GetMapping("/accounts")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();

        return ResponseEntity.ok().body(users);
    }

    @GetMapping("/sessions")
    public ResponseEntity<List<Session>> getAllSessions() {
        List<Session> sessions = sessionService.getAllSessions();

        return ResponseEntity.ok().body(sessions);
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> createUser(@RequestBody @Valid UserCreationRequest request) {
        UserResponseDTO userResponseDTO = userService.createUser(request);

        return ResponseEntity.ok().body(userResponseDTO);
    }

    @PostMapping("/login")
    public ResponseEntity<SessionResponseDTO> loginUserAccount(@RequestBody @Valid UserLoginRequest req) {
        SessionResponseDTO dto = userService.loginUserCredential(req);

        return ResponseEntity.ok().body(dto);
    }

//    @GetMapping("/refresh")
//    public ResponseEntity<UserResponseDTO> refreshUserAccount(@RequestParam String token) {}
}
