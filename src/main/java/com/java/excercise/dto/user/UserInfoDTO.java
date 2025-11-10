package com.java.excercise.dto.user;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserInfoDTO {
    private UserProfileDTO userProfile;
    private UserAddressDTO userAddress;
        ;
}
