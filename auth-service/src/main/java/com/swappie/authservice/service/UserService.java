package com.swappie.authservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.swappie.authservice.domain.Session;
import com.swappie.authservice.domain.User;
import com.swappie.authservice.domain.UserStatus;
import com.swappie.authservice.dto.SessionResponseDTO;
import com.swappie.authservice.dto.UserCreationRequest;
import com.swappie.authservice.dto.UserLoginRequest;
import com.swappie.authservice.dto.UserResponseDTO;
import com.swappie.authservice.dto.event.AccountCreatedEvent;
import com.swappie.authservice.dto.event.AccountCreationFailedEvent;
import com.swappie.authservice.exception.EmailAlreadyExistException;
import com.swappie.authservice.exception.EmailNotFoundException;
import com.swappie.authservice.exception.InvalidPasswordException;
import com.swappie.authservice.helper.PasswordHasher;
import com.swappie.authservice.helper.SessionMapper;
import com.swappie.authservice.helper.UserMapper;
import com.swappie.authservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserEventService userEventService;
    private final SessionService sessionService;
    private final ObjectMapper mapper = new ObjectMapper();

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public UserResponseDTO createUser(UserCreationRequest request) {
        if (userRepository.existsByEmail(request.getEmail()))
            throw new EmailAlreadyExistException("Email already exist");

        User user = UserMapper.toDomain(request);
        user.setPasswordHash(PasswordHasher.hashPassword(request.getPassword()));

        User savedUser = userRepository.save(user);
        userEventService.publishUserCreatedEvent(savedUser);

        return UserMapper.toDTO(savedUser);
    }

    public SessionResponseDTO loginUserCredential(UserLoginRequest req) {
        User existUser = userRepository.findByEmail(req.getEmail()).orElseThrow(() -> new EmailNotFoundException("Email not found"));
        if (!PasswordHasher.matchPassword(req.getPassword(), existUser.getPasswordHash()))
            throw new InvalidPasswordException("Wrong password");

        Session savedSession = sessionService.createSession(existUser);

        return SessionMapper.toDTO(savedSession);
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
