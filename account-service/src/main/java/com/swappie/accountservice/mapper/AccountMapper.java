package com.swappie.accountservice.mapper;

import com.swappie.accountservice.domain.Account;
import com.swappie.accountservice.dto.event.UserCreatedEvent;

public class AccountMapper {
    public static Account toDomain(UserCreatedEvent event) {
        Account account = new Account();

        account.setId(event.getId());

        return account;
    }
}
