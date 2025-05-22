package com.example.spm.pojo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderItem {
    private Long orderId;
    private Long productId;
    private int quantity;
    private String spec;
    private BigDecimal price;
    private int status;

    // Getters and Setters
}