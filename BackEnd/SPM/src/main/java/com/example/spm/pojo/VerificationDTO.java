package com.example.spm.pojo;

import lombok.Data;

@Data
public class VerificationDTO {
    private Integer userId = -1;
    private Integer reviewerId = -1;
    private int status = -1;
    private int type = -1;
    private int page = 0;
    private int limit = 100;
}
