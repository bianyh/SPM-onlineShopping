package com.example.spm.mapper;

import com.example.spm.pojo.Verification;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface VerificationMapper {

        // 添加
        @Select("""
                        INSERT INTO verification(user_id, type, status, content, created_at)
                        VALUES (#{userId}, #{type}, #{status}, #{content}, NOW());
                        select LAST_INSERT_ID();
                        """)
        int add(
                        @Param("type") int type,
                        @Param("userId") Integer userId,
                        @Param("status") int status,
                        @Param("content") String content);

        /*
         * 参数id中存储的是地址对应的用户的id
         */
        @Select("select * from verification where user_id =#{userId}")
        List<Verification> findByUserId(Integer userId);

        

        /*
         * 更新或撤销
         */
        @Update("""
                                update verification set
                                status = #{status},
                                content = #{content},
                                updated_at = now()
                                where id = #{id};
                        """)
        void updateById(@Param("id") Integer id,
                        @Param("content") String content,
                        @Param("status") int status);

        /*
         * 处理
         */
        @Update("""
                                update verification set
                                status = #{status},
                                reply = #{reply},
                                reviewer_id = #{reviewer_id},
                                updated_at = now()
                                where id = #{id};
                        """)
        void settleById(
                        @Param("id") Integer id,
                        @Param("reviewer_id") Integer reviewerId,
                        @Param("reply") String reply,
                        @Param("status") int status);

        @Select("select * from verification where id = #{id}")
        Verification findById(Integer id);

        @Select("select * from verification where type = #{type}")
        List<Verification> findByType(int type);

        @Delete("delete from verification where id = #{id} ")
        void deleteById(Integer id);

        @Delete("delete from verification where type = #{type} ")
        void deleteByType(int type);

        //大搜
        @Select("select * from verification where user_id like #{userId} and reviewer_id like #{reviewerId} and status like #{status} and type like #{type} limit #{page},#{limit}")
        List<Verification> search(String userId, String status, String reviewerId, String type, int page, int limit);
}