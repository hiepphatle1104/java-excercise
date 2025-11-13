package com.java.excercise.dto.admin;

import com.java.excercise.model.enums.UserStatus;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UpdateStatusRequest {
    private UserStatus status;
}
