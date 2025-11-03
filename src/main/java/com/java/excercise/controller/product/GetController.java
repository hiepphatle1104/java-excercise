package com.java.excercise.controller.product;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class GetController {
    @GetMapping("/{id}")
    public ResponseEntity<?> handle(@PathVariable String id) {

        return ResponseEntity.ok().build();
    }
}
