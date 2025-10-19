package com.swappie.authservice.publisher;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper mapper = new ObjectMapper();

    public void publish(UserCreatedEvent event) {
        try {
            // Serialize object → JSON string
            String payload = mapper.writeValueAsString(event);

            // Send event
            CompletableFuture<SendResult<String, String>> future = kafkaTemplate.send(topicName, payload);

            future.whenComplete((result, ex) -> {
                if (ex != null)
                    log.error("Failed to publish event: {}", ex.getMessage());

                log.info("Published event to topic [{}], offset={}", topicName, result.getRecordMetadata().offset());
            });
        } catch (JsonProcessingException e) {
            log.error("Failed to serialize UserCreatedEvent: {}", e.getMessage(), e);
        } catch (Exception e) {
            log.error("Unexpected error while publishing event: {}", e.getMessage(), e);
        }

    }
}
