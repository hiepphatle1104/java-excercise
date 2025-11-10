package com.java.excercise.model.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.time.LocalDate;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserProfile {
    @Column(name = "profile_name") // Đặt tên cột rõ ràng để tránh trùng
    private String name;

    @Column(name = "profile_phone")
    private String phone;

    @Column(name = "profile_birth")
    private LocalDate birth; // Dùng LocalDate cho ngày sinh

    @Column(name = "profile_gender")
    private String gender;
}
