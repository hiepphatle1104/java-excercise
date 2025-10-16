package org.trading.platform.customerservice.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.trading.platform.customerservice.dto.CustomerRequestDTO;
import org.trading.platform.customerservice.dto.CustomerResponseDTO;
import org.trading.platform.customerservice.service.CustomerService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/customers")
public class CustomerController {
    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping
    public ResponseEntity<List<CustomerResponseDTO>> getCustomers() {
        List<CustomerResponseDTO> customers = customerService.getCustomers();

        return ResponseEntity.ok().body(customers);
    }

    @PostMapping
    public ResponseEntity<CustomerResponseDTO> createCustomer(@Valid @RequestBody CustomerRequestDTO customerRequestDTO) {
        CustomerResponseDTO customerResponseDTO = customerService.createCustomer(customerRequestDTO);

        return ResponseEntity.ok().body(customerResponseDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerResponseDTO> getCustomer(@PathVariable UUID id) {
        CustomerResponseDTO customerResponseDTO = customerService.getCustomer(id);

        return ResponseEntity.ok().body(customerResponseDTO);
    }
}
