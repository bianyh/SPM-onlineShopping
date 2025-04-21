package com.example.spm.pojo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Verification {
    private Integer id;
    private Integer type;
    private Integer userId;
    private Integer reviewerId;
    private String content;
    private String reply;
    private LocalDateTime updatedAt;
    private LocalDateTime createdAt;
    private Integer status;
}