package com.example.spm.service.impl;

import com.example.spm.mapper.userMapper;
import com.example.spm.pojo.RegisterDTO;
import com.example.spm.pojo.User;
import com.example.spm.service.userService;
import com.example.spm.utils.Md5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class userServiceImpl implements userService {

    @Autowired
    private userMapper usermapper;

    @Override
    public void register(RegisterDTO user) {
        String md5String = Md5Util.getMD5String(user.getPassword());
        usermapper.add(user.getUsername(), md5String, user.getPhone(), user.getEmail());
    }

    @Override
    public void update(User user) {
        usermapper.update(user.getId(), user.getNickname(), user.getUserPic(), user.getPhone(), user.getEmail());
    }

    @Override
    public User findByUsername(String username){
        User u = usermapper.findByUsername(username);
        return u;
    }

    @Override
    public boolean isAdmin(Integer userId) {
        return usermapper.isAdmin(userId) != 0;
    }
}
