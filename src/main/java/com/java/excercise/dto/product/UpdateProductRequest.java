package com.java.excercise.dto.product;

import com.java.excercise.model.enums.ProductCategory;
import com.java.excercise.model.enums.ProductStatus;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
public class UpdateProductRequest {

//    @NotBlank(message = "category cannot be blank")
    @NotNull(message = "category cannot be blank")
    private ProductCategory category;

    @NotBlank(message = "name cannot be blank")
    private String name;

    @NotBlank(message = "brand cannot be blank")
    private String brand;

    @NotBlank(message = "date cannot be blank")
    private String date;

//    @NotBlank(message = "status cannot be blank")
    @NotNull(message = "status cannot be blank")
    private ProductStatus status;

    @Size(max = 1000, message = "description cannot be greater than 1000 characters")
    private String description;

    @NotNull(message = "price cannot be blank")
    private Double price;

    private List<String> images;

//    @NotBlank(message = "details cannot be blank")
    @NotNull(message = "details cannot be blank")
    @Valid
    private ProductDetailRequest details;
}

