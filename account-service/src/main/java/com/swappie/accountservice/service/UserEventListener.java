package com.swappie.accountservice.service;

import com.swappie.accountservice.dto.event.UserCreatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserEventListener {
    @KafkaListener(topics = "${kafka.topics.account-created}", groupId = "${spring.kafka.consumer.group-id}")
    public void handleUserCreatedEvent(UserCreatedEvent ev) {
        log.info("Received UserCreatedEvent {}", ev);
    }
}
