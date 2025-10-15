package org.trading.platform.customerservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.trading.platform.customerservice.model.Customer;

@SpringBootApplication
public class CustomerServiceApplication {

    static void main(String[] args) {
        SpringApplication.run(CustomerServiceApplication.class, args);
    }

}
