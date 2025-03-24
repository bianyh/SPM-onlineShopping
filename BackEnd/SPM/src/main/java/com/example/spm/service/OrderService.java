package com.example.spm.service;

import com.example.spm.pojo.OrderRequest;

import java.util.Map;

public interface OrderService {


    void submitOrder(Map<String, Object> userInfo, OrderRequest orderRequest);
}
