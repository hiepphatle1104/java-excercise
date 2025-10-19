package com.swappie.accountservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.swappie.accountservice.dto.event.UserCreatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserEventListener {
    private final ObjectMapper mapper = new ObjectMapper();

    @KafkaListener(topics = "${kafka.topics.user-created}", groupId = "${spring.kafka.consumer.group-id}")
    public void handleUserCreatedEvent(String payload) {
        log.info("Received raw message: {}", payload);
        try {
            UserCreatedEvent ev = mapper.readValue(payload, UserCreatedEvent.class);
            log.info("Parsed UserCreatedEvent: {}", ev);
        } catch (Exception e) {
            log.error("Failed to parse message: {}", e.getMessage());
        }
    }
}
