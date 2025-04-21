package com.example.spm.controller;

import com.example.spm.pojo.Result;
import com.example.spm.pojo.ShoppingCartItem;
import com.example.spm.service.ShoppingCartService;
import com.example.spm.utils.ThreadLocalUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.spm.service.VerificationService;

@RestController()
@RequestMapping("verify")
public class VerificationController {

    @Autowired
    private VerificationService vs;
/* 
    @GetMapping("view")
    public Result getVerify() {
        Map<String, Object> map = ThreadLocalUtil.get();
        System.out.println("    ThreadLocalUtil.get() = " + map);
        Integer userId = (Integer) map.get("id");
        System.out.println("    userId = " + userId);
        Integer totalQuantity = vs.getTotalQuantity(userId);
        return Result.success(totalQuantity!= null? totalQuantity : 0);
    }

    @PostMapping("raise")
    public Result addProductToCart(@RequestBody ShoppingCartItem shoppingCartItem) {
        Map<String, Object> map = ThreadLocalUtil.get();
        System.out.println("    ThreadLocalUtil.get() = " + map);
        Integer userId = (Integer) map.get("id"); // 修改为小写的"id"
        System.out.println("    userId = "    + userId);
        shoppingCartItem.setUserId(userId);
        shoppingCartItem.setCreatedAt(new Date());
        Map<String, Object> result = new HashMap<>();
        int rowsAffected = shoppingCartService.addProductToCart(shoppingCartItem);
        if (rowsAffected > 0) {
            result.put("code", 0);
            result.put("message", "成功添加商品到购物车");
            return Result.success(result);
        } else {
            result.put("code", 1);
            result.put("message", "添加商品到购物车失败");
            return Result.error(result.toString());
        }
    } */
}