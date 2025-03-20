package com.example.spm.pojo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@Data
public class Order {
    private Integer id; // 主键
    private Integer userId;
    private Long addressId;
    private BigDecimal totalAmount;
    private String paymentMethod;
    private int status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // getter 和 setter 方法
}