package org.trading.platform.customerservice.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @NotNull
    @Email
    @Column(unique = true)
    private String email;

    @NotNull
    private String hashedPassword;

    private String firstName;
    private String lastName;
    private String address;
    private String phoneNumber;

    @NotNull
    private LocalDate createdDate;

    public Customer(String email, String hashedPassword) {
        this.email = email;
        this.hashedPassword = hashedPassword;
    }
}
