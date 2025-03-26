package com.example.spm.pojo;

import lombok.Data;

@Data
public class LogisticsDTO {
    private Integer orderId;
    private Integer productId;
    private String status;
    private String trackingNumber;
}
