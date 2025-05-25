package com.example.spm.pojo;

import lombok.Data;

@Data
public class OrderItemRecordDTO {
    private Integer orderId;
    private Integer quantity;
    private Double price;
    private String status;
    private String spec;
    private String createdAt;
    private String updatedAt;

    // Add any other fields you need for the order item record
}
