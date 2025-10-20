package com.swappie.authservice.service;

import com.swappie.authservice.domain.Session;
import com.swappie.authservice.domain.User;
import com.swappie.authservice.helper.TokenGenerator;
import com.swappie.authservice.repository.SessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SessionService {
    private static final long ACCESS_TTL_SECONDS = 24 * 3600; // 1 day
    private static final long REFRESH_TTL_SECONDS = 7 * 24 * 3600; // 7 days
    private final SessionRepository sessionRepository;

    public List<Session> getAllSessions() {
        return sessionRepository.findAll();
    }


    // TODO: Enhanced this func
    public Session createSession(User user) {
        Optional<Session> result = sessionRepository.findByUserId(user.getId());

        if (result.isPresent())
            return result.get();

        LocalDateTime now = LocalDateTime.now();
        Session session = Session.builder()
                .userId(user.getId())
                .accessToken(TokenGenerator.generate())
                .refreshToken(TokenGenerator.generate())
                .expiresAt(now.plusSeconds(ACCESS_TTL_SECONDS))
                .refreshExpiresAt(now.plusSeconds(REFRESH_TTL_SECONDS))
                .build();

        return sessionRepository.save(session);
    }
}
