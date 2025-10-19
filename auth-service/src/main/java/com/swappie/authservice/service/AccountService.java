package com.swappie.authservice.service;

import com.swappie.authservice.dto.AccountRequestDTO;
import com.swappie.authservice.mapper.AccountMapper;
import com.swappie.authservice.model.Account;
import com.swappie.authservice.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;

    public Account createAccount(AccountRequestDTO requestDTO) {
        Account account = AccountMapper.toModel(requestDTO);

        return accountRepository.save(account);
    }

    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }
}
