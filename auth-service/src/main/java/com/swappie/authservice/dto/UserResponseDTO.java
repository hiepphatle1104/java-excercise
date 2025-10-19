package com.swappie.authservice.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
public class UserResponseDTO {
    private UUID id;
    private String email;
}
