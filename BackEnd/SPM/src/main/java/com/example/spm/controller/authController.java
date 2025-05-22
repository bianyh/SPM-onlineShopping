package com.example.spm.controller;

import com.example.spm.pojo.User;
import com.example.spm.service.userService;
import com.example.spm.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class authController {
    @Autowired
    private userService userService;

    @GetMapping("/user")
    public User GetUserInfo(){
        Map<String, Object> map = ThreadLocalUtil.get();
        String username = (String)map.get("username");
        return userService.findByUsername(username);
    }
}
