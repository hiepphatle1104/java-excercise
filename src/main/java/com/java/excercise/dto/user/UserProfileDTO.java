package com.java.excercise.dto.user;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class UserProfileDTO {
    private String name;
    private String phone;
    private LocalDate birth; // Spring sẽ tự parse "YYYY-MM-DD"
    private String gender;
}
