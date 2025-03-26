package com.example.spm.service;

import com.example.spm.pojo.MerchantOrderResponse;
import com.example.spm.pojo.Order;
import com.example.spm.pojo.OrderRequest;

import java.util.List;
import java.util.Map;

public interface OrderService {


    void submitOrder(Map<String, Object> userInfo, OrderRequest orderRequest);

    List<Order> findByUserId(Integer userId, String status);

    MerchantOrderResponse findOrdersOfSeller(Integer status);
}
