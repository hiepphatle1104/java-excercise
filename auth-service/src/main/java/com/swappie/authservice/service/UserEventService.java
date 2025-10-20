package com.swappie.authservice.service;

import com.swappie.authservice.domain.User;
import com.swappie.authservice.dto.event.UserCreatedEvent;
import com.swappie.authservice.publisher.EventPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserEventService {
    @Value("${kafka.topics.user-created}")
    private String topicName;

    private final EventPublisher publisher;

    public void publishUserCreatedEvent(User user) {
        UserCreatedEvent ev = new UserCreatedEvent(user.getId());

        publisher.publish(topicName, ev);
    }

}
