package com.example.spm.mapper;

import com.example.spm.pojo.Product;
import com.example.spm.pojo.Store;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface sellerMapper {

    @Select("select * from store where name = #{name}")
    Store findByName(String name);

    @Insert("insert into store(name, user_id, address, create_at)" +
            "values(#{name}, #{userId}, #{address}, now())")
    void addStore(@Param("name") String name, @Param("userId") int userId, String address);

    @Update("update store set " +
            "name = #{name}, " +
            "`description` = #{description}, " +
            "picture_path = #{picturePath}, " +
            "status = #{status}, " +
            "address = #{address} " +
            "where id = #{id}")
    void updateStore(String name, String description, String picturePath, String address, int status, int id);

    @Delete("delete from store where id = #{id}")
    void deleteStore(Integer id);

    @Select("select * from store where user_id = #{userId} ")
    List<Store> findByUserName(Integer userId);

    @Select("select * from store where id = #{id} ")
    Store findById(Integer id);

    List<Store> findByNameContaining(
            @Param("name") String name,
            @Param("offset") int offset,
            @Param("pageSize") int pageSize  // 参数名为 pageSize
    );

    int countByNameContaining(String name);

    List<Product> findAllProducts(Integer storeId, Integer status);
}
