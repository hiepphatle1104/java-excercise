package com.swappie.swappie.core.command.service;

import com.swappie.swappie.common.utils.TokenUtils;
import com.swappie.swappie.domain.SessionRecord;
import com.swappie.swappie.domain.UserRecord;
import com.swappie.swappie.infra.SessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SessionCommandService {

    private final SessionRepository sessionRepository;

    public SessionRecord saveRecord(UserRecord userRecord) {
        SessionRecord record = SessionRecord.builder()
                .accessToken(TokenUtils.generateSecureToken(32))
                .refreshToken(TokenUtils.generateSecureToken(64))
                .expiresIn("3600")
                .user(userRecord)
                .build();

        return sessionRepository.save(record);
    }

    public boolean isRevoked(UserRecord record) {
        return sessionRepository.existsByUser(record);
    }
}
