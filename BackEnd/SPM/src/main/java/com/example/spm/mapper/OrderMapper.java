package com.example.spm.mapper;


import com.example.spm.pojo.LogisticsDTO;
import com.example.spm.pojo.Order;
import com.example.spm.pojo.pro_order;
import org.apache.ibatis.annotations.*;

import java.math.BigDecimal;
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

    @Insert("insert into order_logistics(product_id, order_id, tracking_number, status, created_at, updated_at)" +
            "values (#{productId}, #{orderId}, #{trackingNumber} , 2, now(), now())")
    void updateOrderLogistics(@Param("orderId") Integer orderId, @Param("productId") Integer productId, @Param("trackingNumber") String trackingNumber);

    @Update("update order_logistics set status = 3, updated_at = now()")
    void sendOrder(Integer orderId, String trackingNumber);

    @Update("update order_logistics set status = 4, updated_at = now()")
    void confirmOrder(Integer orderId);

    @Select("select * from `order` where id = #{orderId} and user_id = #{userId} ")
    Order getDetailOfOrder(Integer orderId, Integer userId);

    List<LogisticsDTO> getLogistics(@Param("orderId") Integer orderId, @Param("productId") Integer productId);

    @Update("update order_logistics set status = 5, updated_at = now()")
    void cancelOrder(Integer orderId);

    @Select("SELECT order_item.order_id,order_item.product_id FROM product,store,order_item WHERE product.id=order_item.order_id AND store.id=product.store_id AND store.user_id=#{userId}")
    List<pro_order> getProducts(Integer userId);
}
