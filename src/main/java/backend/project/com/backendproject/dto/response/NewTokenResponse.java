package backend.project.com.backendproject.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NewTokenResponse {
    private String accessToken;
    private String refreshToken;
}
