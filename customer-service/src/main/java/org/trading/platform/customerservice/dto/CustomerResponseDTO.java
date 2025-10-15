package org.trading.platform.customerservice.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class CustomerResponseDTO {
    private UUID id;
    private String email;
    private String firstName;
    private String lastName;
    private String address;
    private String phoneNumber;
}
