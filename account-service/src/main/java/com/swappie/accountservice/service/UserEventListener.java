package com.swappie.accountservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserEventListener {

    private final AccountService accountService;

    @KafkaListener(topics = "${kafka.topics.user-created}", groupId = "${spring.kafka.consumer.group-id}")
    public void handleUserCreatedEvent(String payload) {
        log.info("Received raw message: {}", payload);
        accountService.processUserCreationPayload(payload);
    }
}
