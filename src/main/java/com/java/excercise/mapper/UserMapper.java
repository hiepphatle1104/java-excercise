package com.java.excercise.mapper;

import com.java.excercise.dto.user.UserAddressDTO;
import com.java.excercise.dto.user.UserInfoDTO;
import com.java.excercise.dto.user.UserProfileDTO;
import com.java.excercise.model.entities.UserAddress;
import com.java.excercise.model.entities.UserInfo;
import com.java.excercise.model.entities.UserProfile;

public class UserMapper {
    // DTO
    public static UserProfileDTO toProfileDTO(UserProfile userProfile) {
        UserProfileDTO dto = UserProfileDTO.builder()
            .name(userProfile.getName())
            .phone(userProfile.getPhone())
            .birth(userProfile.getBirth())
            .gender(userProfile.getGender())
            .build();
        return dto;
    }

    public static UserAddressDTO  toAddressDTO(UserAddress userAddress) {
        UserAddressDTO dto = UserAddressDTO.builder()
            .fullName(userAddress.getFullName())
            .phone(userAddress.getPhone())
            .city(userAddress.getCity())
            .ward(userAddress.getWard())
            .address(userAddress.getAddress())
            .build();
        return dto;
    }

    public static UserInfoDTO toUserInfoDTO(UserProfileDTO userProfileDTO, UserAddressDTO userAddressDTO) {
        UserInfoDTO dto = UserInfoDTO.builder()
            .userProfile(userProfileDTO)
            .userAddress(userAddressDTO)
            .build();
        return dto;
    }

    // ENTITY
    public static UserProfile toUserProfile(UserProfileDTO userProfileDTO) {
        UserProfile profile = UserProfile.builder()
            .name(userProfileDTO.getName())
            .phone(userProfileDTO.getPhone())
            .birth(userProfileDTO.getBirth())
            .gender(userProfileDTO.getGender())
            .build();
        return profile;
    }

    public static UserAddress toUserAddress(UserAddressDTO userAddressDTO) {
        UserAddress address = UserAddress.builder()
            .fullName(userAddressDTO.getFullName())
            .phone(userAddressDTO.getPhone())
            .city(userAddressDTO.getCity())
            .ward(userAddressDTO.getWard())
            .address(userAddressDTO.getAddress())
            .build();
        return address;
    }

    public static UserInfo toUserInfo(UserProfile  userProfile, UserAddress   userAddress) {
        UserInfo info = UserInfo.builder()
            .userProfile(userProfile)
            .userAddress(userAddress)
            .build();
        return info;
    }
}
