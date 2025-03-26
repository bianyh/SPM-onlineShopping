package com.example.spm.controller;

import com.example.spm.pojo.Order;
import com.example.spm.service.OrderService;
import com.example.spm.pojo.OrderItem;
import com.example.spm.pojo.OrderRequest;
import com.example.spm.pojo.Result;
import com.example.spm.utils.JwtUtil;
import com.example.spm.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderservice;


    @PostMapping("")
    public Result submitOrder(@RequestHeader("Authorization") String authorization,
                              @RequestBody OrderRequest orderRequest) {
        // 获取解析后的参数
//        Long addressId = orderRequest.getAddressId();
//        String paymentMethod = orderRequest.getPaymentMethod();
//        List<OrderItem> items = orderRequest.getItems();

        Map<String, Object> userInfo = JwtUtil.parseToken(authorization);

        orderservice.submitOrder(userInfo, orderRequest);

        return Result.success();
    }

    @GetMapping("/list")
    public Result ListOrders(@RequestParam(required = false) String status) {
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer userId = (Integer) map.get("id");
        List<Order> orders = orderservice.findByUserId(userId, status);
        return Result.success(orders);
    }

//    @GetMapping("/seller")
//    public  Result ListOrdersOfSeller(Integer status) {
//        return Result.success(orderservice.findOrdersOfSeller(status));
//    }
}

