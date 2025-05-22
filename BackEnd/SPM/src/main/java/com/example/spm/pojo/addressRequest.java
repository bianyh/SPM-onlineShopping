package com.example.spm.pojo;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class addressRequest {
    @NotNull
    private Integer id;
    private String recipient;
    private String phone;
    private String province;
    private String city;
    private String district;
    private String street;
}
