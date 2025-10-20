package com.swappie.accountservice.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaConfig {
    @Value("${kafka.topics.account-created}")
    private String topicAccountCreated;

    @Value("${kafka.topics.account-creation-failed}")
    private String topicAccountCreationFailed;

    @Bean
    public NewTopic accountCreatedTopic() {
        return new NewTopic(topicAccountCreated, 1, (short) 1);
    }

    @Bean
    public NewTopic accountCreationFailedTopic() {
        return new NewTopic(topicAccountCreationFailed, 1, (short) 1);
    }
}
