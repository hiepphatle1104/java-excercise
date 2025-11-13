package com.java.excercise.model.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "user_info")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserInfo {
    @Id
    private String id; // Dùng chung ID với bảng User chính

    private String avtUrl;
    private String publicId;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @MapsId // Đánh dấu rằng 'id' này cũng là Foreign Key
    @JoinColumn(name = "user_id")
    private User user; // Liên kết tới bảng User (chứa email/password)

    // Nhúng model UserProfile vào đây
    @Embedded
    private UserProfile userProfile;

    // Nhúng model UserAddress vào đây
    @Embedded
    private UserAddress userAddress;
}
