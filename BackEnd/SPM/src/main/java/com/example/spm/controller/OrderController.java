package com.example.spm.Controller;

import com.example.spm.Service.OrderService;
import com.example.spm.pojo.OrderItem;
import com.example.spm.pojo.OrderRequest;
import com.example.spm.pojo.Result;
import com.example.spm.utils.JwtUtil;
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






}

