package com.java.excercise.controller.auth;

import com.java.excercise.dto.request.SignUpRequest;
import com.java.excercise.dto.response.ApiResponse;
import com.java.excercise.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth/signup")
@Slf4j
public class SignUpController {
    private final UserService userService;

    @PostMapping
    public ResponseEntity<?> handle(@RequestBody SignUpRequest req) {
        var savedUser = userService.createUser(req);

        var resp = ApiResponse.success("user created success", savedUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(resp);
    }
}
