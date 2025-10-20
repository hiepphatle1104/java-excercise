package com.swappie.authservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountEventListener {

    private final UserService userService;

    @KafkaListener(topics = "${kafka.topics.account-created}", groupId = "${spring.kafka.consumer.group-id}")
    public void handleAccountCreatedEvent(String payload) {
        log.info("Received raw message: {}", payload);
        userService.processAccountCreatedPayload(payload);
    }

    @KafkaListener(topics = "${kafka.topics.account-creation-failed}", groupId = "${spring.kafka.consumer.group-id}")
    public void handleAccountCreationFailedEvent(String payload) {
        log.info("Received raw message: {}", payload);
        userService.processAccountCreationFailedPayload(payload);
    }
}
