package com.java.excercise.dto.product;

public record ProductDetailRequest(
    String batteryPercentage,
    String motorCapacity,
    String maximumDistance,
    String chargingTime,
    String weight
) {
}
