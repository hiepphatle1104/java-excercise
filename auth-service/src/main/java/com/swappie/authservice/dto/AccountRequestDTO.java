package com.swappie.authservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class AccountRequestDTO {
    @NotBlank
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank
    @Length(min = 6, max = 20, message = "Password must be valid")
    private String password;
}
