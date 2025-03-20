package com.example.spm.Mapper;


import com.example.spm.pojo.Order;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Mapper
public interface OrderMapper {

    @Options(useGeneratedKeys = true, keyProperty = "id")
    @Insert("INSERT INTO `order` (user_id, address_id, total_amount, payment_method, status, created_at, updated_at) " +
            "VALUES (#{userId}, #{addressId}, #{totalAmount}, #{paymentMethod}, #{status}, #{createdAt}, #{updatedAt})")
    void submitOrder(Order order);

    @Insert("INSERT INTO order_item (order_id, product_id, quantity, price, spec) " +
            "VALUES (#{orderId}, #{productId}, #{quantity}, #{multiply}, #{spec})")
    void submitOrderItem(Integer orderId, Long productId, int quantity, BigDecimal multiply, String spec);
}
