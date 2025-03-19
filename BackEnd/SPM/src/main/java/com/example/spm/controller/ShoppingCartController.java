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

@RestController()
@RequestMapping("cart")
public class ShoppingCartController {

    @Autowired
    private ShoppingCartService shoppingCartService;

    @GetMapping("quantity")
    public Result getTotalQuantity() {
        Map<String, Object> map = ThreadLocalUtil.get();
        System.out.println("    ThreadLocalUtil.get() = " + map);
        Integer userId = (Integer) map.get("id");
        System.out.println("    userId = " + userId);
        Integer totalQuantity = shoppingCartService.getTotalQuantity(userId);
        return Result.success(totalQuantity!= null? totalQuantity : 0);
    }
    @PostMapping()
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
    }
    @GetMapping()
    public Result getCartItems() {
        try {
            Map<String, Object> map = ThreadLocalUtil.get();
            System.out.println("    ThreadLocalUtil.get() = " + map);
            Integer userId = (Integer) map.get("id"); // 修改为小写的"id"
            System.out.println("    userId = "    + userId);
            List<ShoppingCartItem> cartItems = shoppingCartService.getCartItems(userId);
            System.out.println("    cartItems = " + cartItems);
            if (cartItems == null) {
                return Result.success(null);
            }
            return Result.success(cartItems);
        } catch (Exception e) {

            return Result.error(e.getMessage());
        }
    }
    @PostMapping("update")
    public Result updateCartItem(@RequestBody ShoppingCartItem shoppingCartItem) {
        Map<String, Object> map = ThreadLocalUtil.get();
        System.out.println("    ThreadLocalUtil.get() = " + map);
        Integer userId = (Integer) map.get("id");

        Map<String, Object> result = new HashMap<>();
        int rowsAffected = shoppingCartService.updateCartItem(shoppingCartItem, userId);
        if (rowsAffected > 0) {
            result.put("code", 0);
            result.put("message", "成功更新购物车商品");
            return Result.success(result);
        } else {
            result.put("code", 1);
            result.put("message", "更新购物车商品失败");
            return Result.error(result.toString());
        }
    }
    @DeleteMapping("delete/{cartId}")
    public Result deleteCart(@PathVariable Integer cartId) {
        Map<String, Object> map = ThreadLocalUtil.get();
        System.out.println("    ThreadLocalUtil.get() = " + map);
        Integer userId = (Integer) map.get("id");
        Integer rowsAffected = shoppingCartService.deleteCart(cartId, userId);
        if (rowsAffected > 0) {
            Object result = new HashMap<>();
            return Result.success(result);
        } else {
            return Result.error("删除购物车商品失败");
        }
    }

}