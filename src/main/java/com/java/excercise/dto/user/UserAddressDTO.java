package com.java.excercise.dto.user;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserAddressDTO {
    private String fullName;
    private String phone;
    private String city;
    private String ward;
    private String address;
}
