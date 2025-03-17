package com.example.spm.pojo;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Address {
    private Integer id;
    private Integer userId;
    @NotNull
    private String recipient;
    private String phone;
    private String province;
    private String city;
    private String district;
    private String street;
    private boolean isDefault;//是否为默认地址（1为默认）
    private LocalDateTime createdAt;
}