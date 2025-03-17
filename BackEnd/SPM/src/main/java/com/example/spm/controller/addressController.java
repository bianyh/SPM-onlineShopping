package com.example.spm.controller;

import com.example.spm.pojo.Result;
import com.example.spm.pojo.Address;
import com.example.spm.pojo.addressRequest;
import com.example.spm.service.addressService;
import com.example.spm.utils.JwtUtil;
import com.example.spm.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/address")
public class addressController {

    @Autowired
    private addressService addressService;

    /*
    * 添加收获地址*/
    @PostMapping("/add")
    public Result addAddress(@RequestBody @Validated Address address) {
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer id = (Integer) map.get("id");
        address.setUserId(id);

        System.out.println(address);

        addressService.addAddress(address);
        return Result.success();
    }


    /*
    *获取收货地址的列表*/
    @GetMapping("/getAddress")
    public Result getAddress() {
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer id = (Integer) map.get("id");
        List<Address> addresses = addressService.findByUserId(id);
        return Result.success(addresses);
    }


    /*
     * 获取默认地址id*/
    @GetMapping("/getDefault")
    public Result getDefaultAddress() {
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer userId = (Integer) map.get("id");
        System.out.println(userId);
        Integer id = addressService.findDefault(userId);
        return Result.success("默认地址id为：" + id);
    }

    /*
    * 获取省、市、区等地理数据
    * */
    @GetMapping("/regions")
    public Result getRegions(){
      /*  List<Map<String, Object>> regions = addressService.getRegions();*/
        return Result.success(regions);
    }


    /*
    * 获取单个具体的地址*/
    @GetMapping("/{id}")
    public Result getAddress(@PathVariable("id") Integer id) {
        Address address = addressService.findById(id);
        if (address == null) {
            return Result.error("id不存在");
        }
        return Result.success(address);
    }

    /*
    * 设置默认地址, 参数为地址对应的地址*/
    @PostMapping("/default")
    public Result setDefaultAddress(@RequestBody addressRequest address) {
        Integer id = address.getId();
        Address defaultAddress = addressService.updateById(id);
        return Result.success(defaultAddress);
    }

    /*
    * 修改地址数据，要求必须有地址id，可以有修改后的地址、电话和收件人*/
    @PostMapping("/update")
    public Result updateAddress(@RequestBody @Validated addressRequest address) {
        Integer id = address.getId();
        if (addressService.findById(id) == null) {
            return Result.error("id错误！");
        }
        addressService.updateById(address);
        return Result.success();
    }

    /*
    * 通过地址id删除地址*/
    @PostMapping("/delete")
    public Result deleteAddress(@RequestBody @Validated addressRequest address) {
        Integer id = address.getId();
        if (addressService.findById(id) == null) {
            return Result.error("id不存在！");
        }
        addressService.deleteAddress(id);
        return Result.success();
    }
}
