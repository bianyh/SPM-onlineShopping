package com.example.spm.Service.impl;


import com.example.spm.Mapper.OrderMapper;
import com.example.spm.Service.OrderService;
import com.example.spm.pojo.Order;
import com.example.spm.pojo.OrderItem;
import com.example.spm.pojo.OrderRequest;
import com.example.spm.pojo.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class OrderServiceImpl implements OrderService {


    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private com.example.spm.mapper.productMapper productmapper;

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
            orderMapper.submitOrderItem(order.getId(), i.getProductId(), i.getQuantity(), pro.getPrice().multiply(BigDecimal.valueOf(i.getQuantity())), i.getSpec());
        }

        return;

    }

}
