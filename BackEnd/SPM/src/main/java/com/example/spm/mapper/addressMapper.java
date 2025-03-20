package com.example.spm.mapper;

import com.example.spm.pojo.Address;
import jakarta.validation.constraints.NotEmpty;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

@Mapper
public interface addressMapper {

    @Insert("INSERT INTO address(user_id, recipient, phone, province, city, district, address, is_default, created_at) " +
            "VALUES (#{userId}, #{recipient}, #{phone}, #{province}, #{city}, #{district}, #{street}, #{isDefault}, NOW())")
    void add(
            @Param("userId") Integer userId,
            @Param("recipient") String recipient,
            @Param("phone") String phone,
            @Param("province") String province,
            @Param("city") String city,
            @Param("district") String district,
            @Param("street") String street,
            @Param("isDefault") int isDefault
    );

    /*
    * 参数id中存储的是地址对应的用户的id*/
    @Select("select * from address where user_id =#{userId}")
    List<Address> findByUserId(Integer userId);

    /*
    * 修改对应的地址的is_default的值为 1 */
    @Update("update address set is_default = 1 where id = #{id}")
    void updateDefaultById(Integer id);

    /*
    * 更新相对应的地址id的信息，如果数据为空不修改*/
    void updateById( @Param("id") Integer id,
                     @Param("recipient") String recipient,
                     @Param("phone") String phone,
                     @Param("province") String province,
                     @Param("city") String city,
                     @Param("district") String district,
                     @Param("street") String street);

    @Select("select * from address where id = #{id}")
    Address findById(Integer id);

    @Select("select * from address where user_id =#{userId} and is_default = 1")
    Address findDefault(Integer userId);

    @Delete("delete from address where id = #{id} ")
    void deleteAddress(Integer id);

    List<Map<String, Object>> getRegions();
}