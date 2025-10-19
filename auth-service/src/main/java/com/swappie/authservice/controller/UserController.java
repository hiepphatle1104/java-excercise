package com.swappie.authservice.controller;

import com.swappie.authservice.dto.UserCreationRequest;
import com.swappie.authservice.dto.UserResponseDTO;
import com.swappie.authservice.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserResponseDTO> createUser(@RequestBody @Valid UserCreationRequest request) {
        UserResponseDTO userResponseDTO = userService.createUser(request);

        return ResponseEntity.ok().body(userResponseDTO);
    }
}
