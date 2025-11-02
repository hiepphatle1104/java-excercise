package com.swappie.testproduct.dto;

public record ProductDetailRequest(
        String batteryPercentage,
        String motorCapacity,
        String maximumDistance,
        String chargingTime,
        String weight
) {
}