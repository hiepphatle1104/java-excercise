package org.trading.platform.customerservice.service;

import org.springframework.stereotype.Service;
import org.trading.platform.customerservice.dto.CustomerResponseDTO;
import org.trading.platform.customerservice.mapper.CustomerMapper;
import org.trading.platform.customerservice.model.Customer;
import org.trading.platform.customerservice.repository.CustomerRepository;

import java.util.List;

@Service
public class CustomerService {
    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public List<CustomerResponseDTO> getCustomers() {
        List<Customer> customers = customerRepository.findAll();

        return customers.stream().map(CustomerMapper::toDTO).toList();
    }
}
