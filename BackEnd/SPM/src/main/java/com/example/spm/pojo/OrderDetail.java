package com.example.spm.pojo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class OrderDetail {
    private String orderId;
    private Long addressId;
    private String paymentMethod;
    private BigDecimal paymentAmount;
    private List<OrderItem> items;
}
