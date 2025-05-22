package com.example.spm.pojo;

import lombok.Data;

import java.time.LocalTime;

@Data
public class User {
    private int id;
    private String username;
    private String password;
    private String phone;
    private String email;
    private String nickname; //签名
    private String userPic;//用户头像地址;
    private LocalTime createTime;//创建时间
    private LocalTime updateTime;//更新时间

}
