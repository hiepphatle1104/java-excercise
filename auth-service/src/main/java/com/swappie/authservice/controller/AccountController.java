package com.swappie.authservice.controller;

import com.swappie.authservice.dto.RequestDTO;
import com.swappie.authservice.model.Account;
import com.swappie.authservice.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @PostMapping
    public Account createAccount(@RequestBody RequestDTO requestDTO) {
        return accountService.createAccount(requestDTO);
    }

    @GetMapping
    public List<Account> getAllAccounts() {
        return accountService.getAllAccounts();
    }
}
