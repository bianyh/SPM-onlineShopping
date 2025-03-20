package com.example.spm.Service;

import com.example.spm.pojo.OrderRequest;

import java.util.Map;

public interface OrderService {


    void submitOrder(Map<String, Object> userInfo, OrderRequest orderRequest);
}
