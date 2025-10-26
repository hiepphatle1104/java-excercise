package backend.project.com.backendproject.dto.request;

import lombok.Data;

@Data
public class NewTokenRequest {
    private String refreshToken;
}
