package org.trading.platform.customerservice.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.trading.platform.customerservice.dto.CustomerRequestDTO;
import org.trading.platform.customerservice.dto.CustomerResponseDTO;
import org.trading.platform.customerservice.dto.UpdateRequestDTO;
import org.trading.platform.customerservice.service.CustomerService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

    @PutMapping("/{id}")
    public ResponseEntity<CustomerResponseDTO> updateCustomer(@PathVariable UUID id, @Valid @RequestBody UpdateRequestDTO updateRequestDTO) {
        CustomerResponseDTO customerResponseDTO = customerService.updateCustomer(id, updateRequestDTO);

        return ResponseEntity.ok().body(customerResponseDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteCustomer(@PathVariable UUID id) {
        Map<String, String> response = new HashMap<>();
        customerService.deleteCustomer(id);

        response.put("message", "deleted");
        return ResponseEntity.ok().body(response);
    }
}
