package com.java.excercise.model.entities;

import com.java.excercise.dto.auth.JwtPayload;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

import java.util.concurrent.TimeUnit;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@RedisHash("RedisHashRefreshToken")
public class RedisToken {
    @Id
    private String jwtID;
    private String userID;

    // time to live
    @TimeToLive(unit = TimeUnit.MILLISECONDS)
    private Long timeToLive;

    public static RedisToken map(JwtPayload payload) {
        var expirationTime = payload.getExpirationTime().getTime();
        var issueTime = payload.getIssueTime().getTime();

        return RedisToken.builder()
            .jwtID(payload.getJwtID())
            .userID(payload.getUserID())
            .timeToLive(expirationTime - issueTime)
            .build();
    }
}
