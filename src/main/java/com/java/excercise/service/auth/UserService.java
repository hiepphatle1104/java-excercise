package com.java.excercise.service.auth;

import com.java.excercise.dto.auth.SignUpRequest;
import com.java.excercise.dto.auth.SignUpResponse;
import com.java.excercise.dto.auth.UserResponse;
import com.java.excercise.dto.user.UserAddressDTO;
import com.java.excercise.dto.user.UserInfoDTO;
import com.java.excercise.dto.user.UserProfileDTO;
import com.java.excercise.exception.EmailAlreadyExistsException;
import com.java.excercise.exception.NotFoundException;
import com.java.excercise.mapper.UserMapper;
import com.java.excercise.model.entities.User;
import com.java.excercise.model.entities.UserAddress;
import com.java.excercise.model.entities.UserInfo;
import com.java.excercise.model.entities.UserProfile;
import com.java.excercise.model.enums.UserRole;
import com.java.excercise.repository.UserInfoRepository;
import com.java.excercise.repository.UserRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;
    private final UserInfoRepository userInfoRepo;
    private final EntityManager entityManager;

    public SignUpResponse createUser(SignUpRequest req) {
        if (userRepo.existsByEmail(req.email()))
            throw new EmailAlreadyExistsException();

        // TODO: Need to set default role into USER
        Set<String> roles = new HashSet<>();

        // TODO: Need to delete hardcode
        if (req.email().equals("admin"))
            roles.add(UserRole.ADMIN.toString());
        else
            roles.add(UserRole.USER.toString());

        User user = User.map(req, roles);
        user.setPassword(passwordEncoder.encode(req.password()));

        return SignUpResponse.map(userRepo.save(user));
    }

    // Get user
    public UserResponse getUserDetail(String id) {
        User user = userRepo.findById(id)
            .orElseThrow(() -> new RuntimeException("User not found"));

        return UserResponse.builder()
            .id(user.getId())
            .name(user.getName())
            .email(user.getEmail())
            .roles(user.getRoles())
            .build();

    }

    public User getUserById(String id) {
        Optional<User> result = userRepo.findById(id);
        if (result.isEmpty())
            throw new NotFoundException("user not found", "USER_NOT_FOUND");

        return result.get();
    }

    @Transactional
    public UserInfoDTO updateUserInfo(String userId, UserInfoDTO dto) {
//        return dto;
        // 1. Tìm hoặc tạo mới user info
        UserInfo info = userInfoRepo.findById(userId)
            .orElseGet(() -> createNewUserInfo(userId));

        // 2. lấy profile và address từ dto
        UserProfileDTO profileDTO = dto.getUserProfile();
        UserAddressDTO addressDTO = dto.getUserAddress();

        // 3. update
        // profile
        if (info.getUserProfile() == null) {
            info.setUserProfile(new UserProfile());
        }
        UserProfile profile = info.getUserProfile();

        String name = profileDTO.getName() == null ? info.getUserProfile().getName()  : profileDTO.getName();
        String profilePhone = profileDTO.getPhone() == null ? info.getUserProfile().getPhone()  : profileDTO.getPhone();
        LocalDate birth = profileDTO.getBirth() == null ? info.getUserProfile().getBirth() : profileDTO.getBirth();
        String gender =  profileDTO.getGender() == null ? info.getUserProfile().getGender(): profileDTO.getGender();

        profile.setName(name);
        profile.setPhone(profilePhone);
        profile.setBirth(birth);
        profile.setGender(gender);

        // address
        if (info.getUserAddress() == null) {
            info.setUserAddress(new UserAddress());
        }

        UserAddress addressDto = info.getUserAddress();

        String fullName = addressDTO.getFullName() == null ? info.getUserAddress().getFullName() : addressDTO.getFullName();
        String AddressPhone = addressDTO.getPhone() == null ? info.getUserAddress().getPhone() : addressDTO.getPhone();
        String city =  addressDTO.getCity() == null ? info.getUserAddress().getCity() : addressDTO.getCity();
        String ward = addressDTO.getWard() == null ? info.getUserAddress().getWard() : addressDTO.getWard();
        String address =  addressDTO.getAddress() == null ? info.getUserAddress().getAddress() : addressDTO.getAddress();

        addressDto.setFullName(fullName);
        addressDto.setPhone(AddressPhone);
        addressDto.setCity(city);
        addressDto.setWard(ward);
        addressDto.setAddress(address);

        userInfoRepo.save(info);
        return UserMapper.toUserInfoDTO(
            UserMapper.toProfileDTO(info.getUserProfile()),
            UserMapper.toAddressDTO(info.getUserAddress())
        );
    }

    // Hàm tiện ích để tạo UserInfo lần đầu
    private UserInfo createNewUserInfo(String userId) {
        // 1. tìm user
        User user = getUserById(userId);
        // tạo info
        UserInfo newUserInfo = new UserInfo();
        newUserInfo.setId(userId);
        newUserInfo.setUser(user);
        entityManager.persist(newUserInfo);
        return newUserInfo;
    }

    public UserInfoDTO getUserInfo(String userId) {
        Optional<UserInfo> info = userInfoRepo.findById(userId);
        if (info.isEmpty())
            throw new NotFoundException("user not found", "USER_NOT_FOUND");
        return UserMapper.toUserInfoDTO(
            UserMapper.toProfileDTO(info.get().getUserProfile()),
            UserMapper.toAddressDTO(info.get().getUserAddress())
        );
    }
}
