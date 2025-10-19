package com.swappie.authservice.publisher;

import com.swappie.authservice.dto.event.UserCreatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserPublisher {
    @Value("${kafka.topics.user-created}")
    private String topicName;

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void publish(UserCreatedEvent event) {
        CompletableFuture<SendResult<String, Object>> future = kafkaTemplate.send(topicName, event);
        future.whenComplete((r, e) -> {
            if (e != null)
                log.warn("Publishing failed: {}", e.getMessage());

            log.info("Published user with id: {}", event.getId());
        });
    }
}
