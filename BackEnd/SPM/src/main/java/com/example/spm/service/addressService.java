package com.example.spm.service;

import com.example.spm.pojo.Address;
import com.example.spm.pojo.addressRequest;

import java.util.List;

public interface addressService {

//    添加地址
    void addAddress(Address address);

//    通过用户的id查找对应的地址
    List<Address> findByUserId(Integer id);

//    通过地址id更新
    Address updateById(Integer id);

    Address findById(Integer id);

    void updateById(addressRequest address);

//    查找默认地址
    Integer findDefault(Integer userId);
}
