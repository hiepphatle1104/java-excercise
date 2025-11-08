package com.java.excercise.model.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserAddress {
    @Column(name = "address_full_name")
    private String fullName;

    @Column(name = "address_phone")
    private String phone; // SĐT này của địa chỉ, phân biệt với SĐT của profile

    @Column(name = "address_city")
    private String city;

    @Column(name = "address_ward")
    private String ward;

    @Column(name = "address_detail") // 'address' là từ khóa của SQL, nên đổi tên
    private String address;
}
