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
    private int status; //0 未支付，1 已支付，2 待发货，3 运输中，4 已完成 ，5 已取消
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // getter 和 setter 方法
}