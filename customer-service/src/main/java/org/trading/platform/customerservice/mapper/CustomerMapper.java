package org.trading.platform.customerservice.mapper;

import org.trading.platform.customerservice.dto.CustomerResponseDTO;
import org.trading.platform.customerservice.model.Customer;

public class CustomerMapper {
    public static CustomerResponseDTO toDTO(Customer customer) {
        CustomerResponseDTO dto = new CustomerResponseDTO();

        dto.setId(customer.getId());
        dto.setEmail(customer.getEmail());
        dto.setFirstName(customer.getFirstName());
        dto.setLastName(customer.getLastName());
        dto.setAddress(customer.getAddress());
        dto.setPhoneNumber(customer.getPhoneNumber());

        return dto;
    }
}
