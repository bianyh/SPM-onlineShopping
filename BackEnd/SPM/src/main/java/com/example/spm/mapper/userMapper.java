package com.example.spm.mapper;

import com.example.spm.pojo.User;
import org.apache.ibatis.annotations.*;

@Mapper
public interface userMapper {

    @Insert("insert into user(username, password, phone, email,  created_at, updated_at)" +
            "values(#{username}, #{password},#{phone}, #{email}, now(), now())")
    public void add(@Param("username") String username, @Param("password") String password, @Param("phone") String phone, String email);

    @Select("select * from user where username=#{username}")
    public User findByUsername(String username);

    @Update("update user set nickname=#{nickname}, avatar=#{userpic}, phone=#{phone}, email=#{email} where id=#{id}")
    void update(@Param("id") int id, @Param("nickname") String nickname, @Param("userpic") String userpic, @Param("phone") String phone, String email);

    @Select("SELECT EXISTS(SELECT 1 FROM admin WHERE user_id = #{userId});")
    int isAdmin(Integer userId);
}
