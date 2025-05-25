package com.example.spm.pojo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class OrderItem {
    private Long orderId;
    private Long productId;
    private int quantity;
    private String spec;
    private BigDecimal price;
    private int status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Getters and Setters
}