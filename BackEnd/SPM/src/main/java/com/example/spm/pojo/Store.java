package com.example.spm.pojo;

import jakarta.persistence.Column;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Store {
    private int id;
    private String name;
    private String userName;
    private String address;
    private String description;//商店的介绍
    private String picturePath;
    @Column(name = "create_at")
    private LocalDateTime createAt;
    private int status; //商店的状态（0为打烊，1为营业，2为审核中）
    private int userId;

}
