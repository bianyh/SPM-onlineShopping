package com.example.spm.pojo;

import lombok.Data;

@Data
public class Comment {
    private Integer orderId;
    private Integer productId;
    private Integer rating;
    private String comment;
}