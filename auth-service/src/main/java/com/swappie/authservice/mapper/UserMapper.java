package com.swappie.authservice.mapper;

import com.swappie.authservice.domain.User;
import com.swappie.authservice.domain.UserStatus;
import com.swappie.authservice.dto.UserCreationRequest;
import com.swappie.authservice.dto.UserResponseDTO;

public class UserMapper {
    public static User toDomain(UserCreationRequest request) {
        User user = new User();

        user.setStatus(UserStatus.PENDING);
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());

        return user;
    }

    public static UserResponseDTO toDTO(User user) {
        UserResponseDTO dto = new UserResponseDTO();

        dto.setId(user.getId());
        dto.setEmail(user.getEmail());

        return dto;
    }
}
