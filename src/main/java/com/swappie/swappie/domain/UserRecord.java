package com.swappie.swappie.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(unique = true)
    private String username;

    @Setter
    private String passwordHash;

    @Setter
    @Enumerated(EnumType.STRING)
    private UserStatus status = UserStatus.INACTIVE;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;
}
