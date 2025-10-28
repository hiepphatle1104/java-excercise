package com.swappie.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GeneratedToken {
    private String token;
    private JwtPayload jwtPayload;
}
