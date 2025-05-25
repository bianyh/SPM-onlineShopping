package com.example.spm.service.impl;


import com.example.spm.mapper.OrderMapper;
import com.example.spm.pojo.*;
import com.example.spm.service.OrderService;
import com.example.spm.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class OrderServiceImpl implements OrderService {


    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private com.example.spm.mapper.productMapper productmapper;

    @Override
    public void updateOrderLogistics(Integer orderId, Integer productId) {
        // 使用 Java 8 时间 API（线程安全）
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS");
        String timestamp = LocalDateTime.now().format(formatter);

        // 生成随机后缀（0~999）
        int randomSuffix = new Random().nextInt(1000);
        String trackingNumber = timestamp + "-" + String.format("%03d", randomSuffix);
        orderMapper.updateOrderLogistics(orderId, productId, trackingNumber);
    }

    @Override
    @Transactional
    public void submitOrder(Map<String, Object> userInfo, OrderRequest orderRequest) {
        // 获取解析后的参数
        Long addressId = orderRequest.getAddressId();
        String paymentMethod = orderRequest.getPaymentMethod();
        List<OrderItem> items = orderRequest.getItems();

        Integer userId = (Integer) userInfo.get("id");
        String userName = (String) userInfo.get("username");

        BigDecimal totalAmount = BigDecimal.valueOf(0L);
        for (OrderItem i : items) {
            Product pro = productmapper.getProductById(Math.toIntExact(i.getProductId()));
            totalAmount = totalAmount.add(pro.getPrice().multiply(BigDecimal.valueOf(i.getQuantity())));
        }

        Integer id = Integer.valueOf(0);
        // 创建 Order 对象
        Order order = new Order();
        order.setUserId(userId);
        order.setAddressId(addressId);
        order.setTotalAmount(totalAmount);
        order.setPaymentMethod(paymentMethod);
        order.setStatus(0);
        order.setCreatedAt(LocalDateTime.now());
        order.setUpdatedAt(LocalDateTime.now());

        // 调用 submitOrder 方法，直接传递 Order 对象
        orderMapper.submitOrder(order);

        for (OrderItem i : items) {
            Product pro = productmapper.getProductById(Math.toIntExact(i.getProductId()));
            //spec被替换为userid
            orderMapper.submitOrderItem(order.getId(), i.getProductId(), i.getQuantity(), pro.getPrice().multiply(BigDecimal.valueOf(i.getQuantity())), userId.toString(), i.getStatus(), order.getCreatedAt().toString(), order.getUpdatedAt().toString());
        }
    }

    @Override
    public List<Order> findByUserId(Integer userId, String status) {
        if(status == null){
            return orderMapper.findByUserId(userId);
        }
        return orderMapper.findByUserIdAndStatus(userId, status);
    }

    @Override
    public void sendOrder(Integer orderId, Integer productId, String trackingNumber) {
        if (orderMapper.getLogisticsByProductId(orderId, productId) != null) {
            orderMapper.updateOrderLogistics(orderId, productId, trackingNumber);
            return;
        }
        orderMapper.createOrderLogistics(orderId, productId, trackingNumber);
    }

    @Override
    public void confirmOrder(Integer orderId) {
        orderMapper.confirmOrder(orderId);
        orderMapper.updateOrderStatus(orderId, 4);
    }

    @Override
    public Order getDetail(Integer orderId) {
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer userId = (Integer) map.get("id");
        return orderMapper.getDetailOfOrder(orderId,userId);
    }

    @Override
    public List<OrderItem> getOrderItems(Integer orderId) {
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer userId = (Integer) map.get("id");
        return orderMapper.findProductsByOrder(orderId);
    }
    
    @Override
    public List<LogisticsDTO> getLogistics(Integer orderId) {
        return orderMapper.getLogistics(orderId);
    }
    @Override
    public LogisticsDTO getLogisticsByProductId(Integer orderId, Integer productId) {
        return orderMapper.getLogisticsByProductId(orderId, productId);
    }

    @Override
    public Map<Integer, List<Integer>> getOrder(Integer userId, Integer status) {
        Map<Integer, List<Integer>> ans = new HashMap<>();
        List<pro_order> products = orderMapper.getProducts(userId);
        for (pro_order i : products) {
            List<Integer> cache = ans.getOrDefault(i.getProductId(), new ArrayList<>());
            cache.add(i.getOrderId());
            ans.put(i.getProductId(), cache);
        }
        return ans;
    }

    @Override
    public void cancelOrder(Integer orderId) {
        orderMapper.cancelOrder(orderId);
    }

    @Override
    public void submitComment(Integer orderId, Integer productId, Integer rating, String comment) {
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer userId = (Integer) map.get("id");
        orderMapper.submitComment(userId, orderId, productId, rating, comment);
    }

    @Override
    public void updateOrderStatus(Integer orderId, Integer status) {
        orderMapper.updateOrderStatus(orderId,status);
        orderMapper.updateOrderItemStatus(orderId,status);
    }

    @Override
    public int checkOrder(Integer userId, Integer productId) {
        return orderMapper.checkOrder(userId, productId);
    }
}
