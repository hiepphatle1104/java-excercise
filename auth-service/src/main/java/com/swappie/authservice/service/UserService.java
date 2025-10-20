package com.swappie.authservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.swappie.authservice.domain.User;
import com.swappie.authservice.domain.UserStatus;
import com.swappie.authservice.dto.UserCreationRequest;
import com.swappie.authservice.dto.UserResponseDTO;
import com.swappie.authservice.dto.event.AccountCreatedEvent;
import com.swappie.authservice.dto.event.AccountCreationFailedEvent;
import com.swappie.authservice.exception.EmailAlreadyExistException;
import com.swappie.authservice.mapper.UserMapper;
import com.swappie.authservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserEventService userEventService;
    private final ObjectMapper mapper = new ObjectMapper();

    public UserResponseDTO createUser(UserCreationRequest request) {
        if (userRepository.existsByEmail(request.getEmail()))
            throw new EmailAlreadyExistException("Email already exist");

        User savedUser = userRepository.save(UserMapper.toDomain(request));
        userEventService.publishUserCreatedEvent(savedUser);

        return UserMapper.toDTO(savedUser);
    }

    @Transactional
    public void processAccountCreatedPayload(String payload) {
        try {
            AccountCreatedEvent ev = mapper.readValue(payload, AccountCreatedEvent.class);
            log.info("Parsed AccountCreatedEvent: {}", ev);

            Optional<User> record = userRepository.findById(ev.getId());
            record.ifPresent(user -> user.setStatus(UserStatus.ACTIVE));

        } catch (Exception e) {
            log.error("Failed to process account creation payload: {}", payload, e);
        }
    }

    @Transactional
    public void processAccountCreationFailedPayload(String payload) {
        try {
            AccountCreationFailedEvent ev = mapper.readValue(payload, AccountCreationFailedEvent.class);
            log.info("Parsed AccountCreationFailedEvent: {}", ev);

            Optional<User> record = userRepository.findById(ev.getId());
            record.ifPresent(userRepository::delete);

        } catch (Exception e) {
            log.error("Failed to process account creation payload: {}", payload, e);
        }
    }
}
