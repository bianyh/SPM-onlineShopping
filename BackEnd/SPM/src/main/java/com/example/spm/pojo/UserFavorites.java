package com.example.spm.pojo;

import lombok.Data;

import java.util.Date;

@Data
public class UserFavorites {
    private Integer userId;
    private Integer productId;
    private Date createdAt;
}
