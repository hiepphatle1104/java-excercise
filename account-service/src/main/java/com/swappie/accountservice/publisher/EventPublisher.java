package com.swappie.accountservice.publisher;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Slf4j
@Component
@RequiredArgsConstructor
public class EventPublisher {
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper mapper;


    public <T> void publish(String topicName, T event) {
        try {
            String payload = mapper.writeValueAsString(event);
            CompletableFuture<SendResult<String, String>> future = kafkaTemplate.send(topicName, payload);

            future.whenComplete((result, ex) -> {
                if (ex == null) {
                    log.info("Published event to topic [{}], offset=[{}], eventType=[{}]",
                            topicName, result.getRecordMetadata().offset(), event.getClass().getSimpleName());
                } else {
                    log.error("Failed to publish event: {}", ex.getMessage());
                }
            });
        } catch (JsonProcessingException e) {
            log.error("Failed to serialize event: {}", e.getMessage(), e);
        } catch (Exception e) {
            log.error("Unexpected error while publishing event: {}", e.getMessage(), e);
        }
    }

}
