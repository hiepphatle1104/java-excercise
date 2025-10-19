package com.swappie.authservice.mapper;

import com.swappie.authservice.dto.RequestDTO;
import com.swappie.authservice.model.Account;

public class AccountMapper {
    public static Account toModel(RequestDTO requestDTO) {
        Account account = new Account();

        account.setEmail(requestDTO.getEmail());
        account.setHashedPassword(requestDTO.getPassword());

        return account;
    }
}
