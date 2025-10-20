package com.swappie.accountservice.service;

import com.swappie.accountservice.domain.Account;
import com.swappie.accountservice.dto.event.AccountCreatedEvent;
import com.swappie.accountservice.dto.event.AccountCreationFailedEvent;
import com.swappie.accountservice.dto.event.UserCreatedEvent;
import com.swappie.accountservice.publisher.EventPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountEventService {

    private final EventPublisher publisher;

    @Value("${kafka.topics.account-created}")
    private String topicAccountCreated;

    @Value("${kafka.topics.account-creation-failed}")
    private String topicAccountCreationFailed;

    public void publishAccountCreatedEvent(Account account) {
        AccountCreatedEvent event = new AccountCreatedEvent();
        event.setId(account.getId());
        event.setMessage("Account created successfully for user: " + account.getId());

        publisher.publish(topicAccountCreated, event);
    }

    public void publishAccountCreationFailedEvent(UserCreatedEvent originalEvent, Exception error) {
        AccountCreationFailedEvent event = new AccountCreationFailedEvent();
        event.setId(originalEvent.getId());
        event.setErrorMessage(error.getMessage());

        publisher.publish(topicAccountCreationFailed, event);
    }
}
