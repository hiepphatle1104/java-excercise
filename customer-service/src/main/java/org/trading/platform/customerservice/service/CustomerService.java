package org.trading.platform.customerservice.service;

import org.springframework.stereotype.Service;
import org.trading.platform.customerservice.dto.CustomerRequestDTO;
import org.trading.platform.customerservice.dto.CustomerResponseDTO;
import org.trading.platform.customerservice.exception.CustomerNotFoundException;
import org.trading.platform.customerservice.exception.EmailAlreadyExistException;
import org.trading.platform.customerservice.helper.CustomerMapper;
import org.trading.platform.customerservice.model.Customer;
import org.trading.platform.customerservice.repository.CustomerRepository;

import java.util.List;
import java.util.UUID;

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

    public CustomerResponseDTO createCustomer(CustomerRequestDTO customerRequestDTO) {
        if (customerRepository.existsByEmail(customerRequestDTO.getEmail()))
            throw new EmailAlreadyExistException("Email already exist");

        Customer customer = customerRepository.save(CustomerMapper.toModel(customerRequestDTO));

        return CustomerMapper.toDTO(customer);
    }

    public CustomerResponseDTO getCustomer(UUID id) {
        Customer customer = customerRepository.findById(id).orElseThrow(() -> new CustomerNotFoundException("Customer not found"));

        return CustomerMapper.toDTO(customer);
    }
}
