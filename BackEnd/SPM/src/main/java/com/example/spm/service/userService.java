package com.example.spm.service;

import com.example.spm.pojo.RegisterDTO;
import com.example.spm.pojo.User;

public interface userService {
    User findByUsername(String username);

    void register(RegisterDTO user);

    void update(User user);

    boolean isAdmin(Integer userId);
}
