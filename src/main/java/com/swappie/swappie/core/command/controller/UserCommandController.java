package com.swappie.swappie.core.command.controller;

import com.swappie.swappie.common.dto.AppResponse;
import com.swappie.swappie.common.dto.SignInResponse;
import com.swappie.swappie.common.dto.SignUpResponse;
import com.swappie.swappie.core.command.handler.UserCommandHandler;
import com.swappie.swappie.core.command.models.SignInCommand;
import com.swappie.swappie.core.command.models.SignUpCommand;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class UserCommandController {
    private final UserCommandHandler commandHandler;

    @PostMapping("/signup")
    public ResponseEntity<AppResponse<SignUpResponse>> signup(@RequestBody @Valid SignUpCommand command) {
        SignUpResponse resp = commandHandler.handle(command);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(AppResponse.success("USER_CREATED", "user created", resp));
    }

    @PostMapping("/signin")
    public ResponseEntity<AppResponse<SignInResponse>> signin(@RequestBody @Valid SignInCommand command) {
        SignInResponse resp = commandHandler.handle(command);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(AppResponse.success("USER_AUTHENTICATED", "user authenticated", resp));
    }
}
