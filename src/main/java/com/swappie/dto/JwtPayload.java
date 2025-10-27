package com.swappie.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Date;
import java.util.Set;

@Data
@Builder
public class JwtPayload {
    private String userID;
    private String jwtID;
    private Date issueTime;
    private Date expirationTime;
    private Set<String> roles;
}
