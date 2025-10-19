package com.swappie.authservice.mapper;

import com.swappie.authservice.dto.AccountRequestDTO;
import com.swappie.authservice.model.Account;

public class AccountMapper {
    public static Account toModel(AccountRequestDTO requestDTO) {
        Account.AccountBuilder accountBuilder = Account.builder();

        accountBuilder.email(requestDTO.getEmail());
        accountBuilder.hashedPassword(requestDTO.getPassword());

        return accountBuilder.build();
    }
}
