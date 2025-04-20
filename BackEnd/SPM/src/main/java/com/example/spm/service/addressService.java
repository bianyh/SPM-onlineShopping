package com.example.spm.service;

import com.example.spm.pojo.Address;
import com.example.spm.pojo.addressRequest;

import java.util.List;
import java.util.Map;

public interface addressService {

//    添加地址
    void addAddress(Address address);

//    通过用户的id查找对应的地址
    List<Address> findByUserId(Integer id);

//    通过地址id更新
    Address updateDefaultById(Integer id, Integer uid);

    Address findById(Integer id);

    void updateById(addressRequest address);

//    查找默认地址
    Integer findDefault(Integer userId);

//    删除id对应的地址
    void deleteAddress(Integer id);

//    调用高德地图api获得相关地理信息
    List<Map<String, Object>> getRegions();
}
