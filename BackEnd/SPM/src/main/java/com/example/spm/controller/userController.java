package com.example.spm.controller;

import com.example.spm.pojo.LoginDTO;
import com.example.spm.pojo.RegisterDTO;
import com.example.spm.pojo.Result;
import com.example.spm.pojo.User;
import com.example.spm.service.userService;
import com.example.spm.utils.JwtUtil;
import com.example.spm.utils.Md5Util;
import com.example.spm.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@Validated
public class userController {

    @Autowired
    private userService userservice;

    /*
     * 注册账号，要求四个参数：用户名、密码、手机号和邮箱
     */
    @PostMapping("/register")
    public Result register(@RequestBody @Validated RegisterDTO user) {
        // 检验数据库中是否存在相同的用户
        User u = userservice.findByUsername(user.getUsername());
        if (u != null) {
            return Result.error("该用户名已存在！");
        }

        // 添加用户
        userservice.register(user);
        return Result.success();
    }

    @PostMapping("/login")
    public Result login(@RequestBody @Validated LoginDTO user) {
        // 判断用户是否存在
        User u = userservice.findByUsername(user.getUsername());
        if (u == null) {
            return Result.error("该用户不存在！");
        }

        // 判断密码是否正确
        if (!u.getPassword().equals(Md5Util.getMD5String(user.getPassword()))) {
            return Result.error("密码错误！");
        }

        // 密码正确后生成jwt密钥
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", u.getId());
        claims.put("username", u.getUsername());
        claims.put("admin", userservice.isAdmin(u.getId()));
        String token = JwtUtil.genToken(claims);
        return Result.success(token);// 将token发送前端作为数据data部分
    }

    @PostMapping("/update")
    public Result updateUserInfo(@RequestBody @Validated User user) {
        Map<String, Object> map = ThreadLocalUtil.get();
        String username = (String) map.get("username");

        User u = userservice.findByUsername(username);
        Integer id = u.getId();
        user.setId(id);
        userservice.update(user);
        return Result.success();
    }
}
