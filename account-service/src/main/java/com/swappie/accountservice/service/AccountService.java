package com.swappie.accountservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.swappie.accountservice.domain.Account;
import com.swappie.accountservice.dto.event.UserCreatedEvent;
import com.swappie.accountservice.mapper.AccountMapper;
import com.swappie.accountservice.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountService {

    private final ObjectMapper mapper;
    private final AccountRepository accountRepository;
    private final AccountEventService accountEventService;

    @Transactional
    public void processUserCreationPayload(String payload) {
        UserCreatedEvent userEvent = null;
        try {
            userEvent = mapper.readValue(payload, UserCreatedEvent.class);
            log.info("Parsed UserCreatedEvent: {}", userEvent);

            Account account = AccountMapper.toDomain(userEvent);
            Account savedAccount = accountRepository.save(account);

            accountEventService.publishAccountCreatedEvent(savedAccount);
        } catch (Exception e) {
            log.error("Failed to process user creation payload: {}", payload, e);

            if (userEvent != null)
                accountEventService.publishAccountCreationFailedEvent(userEvent, e);

            throw new RuntimeException("Failed to process payload, transaction will be rolled back.");
        }
    }

}
