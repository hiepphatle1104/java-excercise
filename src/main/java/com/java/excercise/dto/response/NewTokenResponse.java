package com.java.excercise.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class NewTokenResponse {
    private String accessToken;
    private String refreshToken;
}
