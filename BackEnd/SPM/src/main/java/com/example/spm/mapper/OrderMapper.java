package com.example.spm.mapper;


import com.example.spm.pojo.Order;
import com.example.spm.pojo.OrderItem;
import org.apache.ibatis.annotations.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface OrderMapper {

    @Options(useGeneratedKeys = true, keyProperty = "id")
    @Insert("INSERT INTO `order` (user_id, address_id, total_amount, payment_method, status, created_at, updated_at) " +
            "VALUES (#{userId}, #{addressId}, #{totalAmount}, #{paymentMethod}, #{status}, #{createdAt}, #{updatedAt})")
    void submitOrder(Order order);

    @Insert("INSERT INTO order_item (order_id, product_id, quantity, price, spec) " +
            "VALUES (#{orderId}, #{productId}, #{quantity}, #{multiply}, #{spec})")
    void submitOrderItem(Integer orderId, Long productId, int quantity, BigDecimal multiply, String spec);

    @Select("select * from `order` where user_id = #{userId} ")
    List<Order> findByUserId(Integer userId);

    @Select("select * from `order` where user_id = #{userId} and status = #{status} ")
    List<Order> findByUserIdAndStatus(@Param("userId") Integer userId, @Param("status") String status);

    @Select("select * from order_item where product_id in #{productIds}")
    List<OrderItem> findByProductIdsAndStatus(@Param("productIds") List<Long> productIds);
}
