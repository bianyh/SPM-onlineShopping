package com.example.spm.pojo;

import lombok.Data;

@Data
public class Comment {
    Integer orderId;
    Integer productId;
    Integer rating;
    String  comment;
}
