package com.swappie.authservice.service;

import com.swappie.authservice.domain.Session;
import com.swappie.authservice.domain.User;
import com.swappie.authservice.helper.TokenGenerator;
import com.swappie.authservice.repository.SessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SessionService {
    private static final long ACCESS_TTL_SECONDS = 24 * 3600; // 1 day
    private static final long REFRESH_TTL_SECONDS = 7 * 24 * 3600; // 7 days
    private final SessionRepository sessionRepository;

    public List<Session> getAllSessions() {
        return sessionRepository.findAll();
    }


    public Session createSession(User user) {
        Session session = new Session();
        LocalDateTime now = LocalDateTime.now();

        session.setUser(user);
        session.setAccessToken(TokenGenerator.generateToken());
        session.setRefreshToken(TokenGenerator.generateToken());
        session.setExpiresAt(now.plusSeconds(ACCESS_TTL_SECONDS));
        session.setRefreshExpiresAt(now.plusSeconds(REFRESH_TTL_SECONDS));

        return sessionRepository.save(session);
    }
}
