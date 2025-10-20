package com.swappie.authservice.helper;

import com.swappie.authservice.domain.Session;
import com.swappie.authservice.dto.SessionResponseDTO;

public class SessionMapper {
    public static SessionResponseDTO toDTO(Session session) {
        SessionResponseDTO dto = new SessionResponseDTO();

        dto.setAccessToken(session.getAccessToken());
        dto.setExpiresAt(session.getExpiresAt());

        return dto;
    }
}
