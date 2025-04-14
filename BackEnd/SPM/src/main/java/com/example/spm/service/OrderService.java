package com.example.spm.service;

import com.example.spm.pojo.LogisticsDTO;
import com.example.spm.pojo.Order;
import com.example.spm.pojo.OrderItem;
import com.example.spm.pojo.OrderRequest;

import java.util.List;
import java.util.Map;

public interface OrderService {


   void updateOrderLogistics(Integer orderId, Integer productId);

   void updateOrderStatus(Integer orderId, Integer status);

    void submitOrder(Map<String, Object> userInfo, OrderRequest orderRequest);

    List<Order> findByUserId(Integer userId, String status);

    void sendOrder(Integer orderId, String trackingNumber);

    void confirmOrder(Integer orderId);

    Order getDetail(Integer orderId);

    List<OrderItem> getOrderItems(Integer orderId);

    List<LogisticsDTO> getLogistics(Integer orderId, Integer productId);

    Map<Integer, List<Integer>> getOrder(Integer userId, Integer status);

    void cancelOrder(Integer orderId);


}
