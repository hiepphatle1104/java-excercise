package com.swappie.authservice.service;

import com.swappie.authservice.domain.User;
import com.swappie.authservice.dto.UserCreationRequest;
import com.swappie.authservice.dto.UserResponseDTO;
import com.swappie.authservice.dto.event.UserCreatedEvent;
import com.swappie.authservice.mapper.UserMapper;
import com.swappie.authservice.publisher.UserPublisher;
import com.swappie.authservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserPublisher publisher;

    public UserResponseDTO createUser(UserCreationRequest request) {
        // Check user exist
        // TODO: Handle exception here
        if (userRepository.existsByEmail(request.getEmail()))
            return null;

        // Save value to database
        User savedUser = userRepository.save(UserMapper.toDomain(request));

        // Create "UserCreatedEvent"
        UserCreatedEvent ev = new UserCreatedEvent(savedUser.getId());

        // Send value to kafka
        // TODO: Outbox pattern here
        publisher.publish(ev);

        return UserMapper.toDTO(savedUser);
    }
}
