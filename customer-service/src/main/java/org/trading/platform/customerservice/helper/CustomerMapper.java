package org.trading.platform.customerservice.helper;

import org.trading.platform.customerservice.dto.CustomerRequestDTO;
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
        dto.setCreatedAt(customer.getCreatedAt());
        dto.setUpdatedAt(customer.getUpdatedAt());

        return dto;
    }

    public static Customer toModel(CustomerRequestDTO customerRequestDTO) {
        Customer customer = new Customer();

        customer.setEmail(customerRequestDTO.getEmail());
        customer.setHashedPassword(customerRequestDTO.getPassword());

        return customer;
    }
}
