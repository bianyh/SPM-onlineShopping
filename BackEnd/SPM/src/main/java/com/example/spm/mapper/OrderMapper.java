package com.example.spm.mapper;

import com.example.spm.pojo.LogisticsDTO;
import com.example.spm.pojo.Order;
import com.example.spm.pojo.OrderItem;
import com.example.spm.pojo.pro_order;
import org.apache.ibatis.annotations.*;

import java.math.BigDecimal;
import java.util.List;

@Mapper
public interface OrderMapper {
        @Options(useGeneratedKeys = true, keyProperty = "id")
        @Insert("INSERT INTO `order` (user_id, address_id, total_amount, payment_method, status, created_at, updated_at) "
                        +
                        "VALUES (#{userId}, #{addressId}, #{totalAmount}, #{paymentMethod}, #{status}, #{createdAt}, #{updatedAt})")
        void submitOrder(Order order);

        @Insert("INSERT INTO order_item (order_id, product_id, quantity, price, spec, status, created_at, updated_at) "
                        +
                        "VALUES (#{orderId}, #{productId}, #{quantity}, #{multiply}, #{spec}, #{status}, #{createdAt}, #{updatedAt})")
        void submitOrderItem(Integer orderId, Long productId, int quantity, BigDecimal multiply, String spec,
                        int status,
                        String createdAt, String updatedAt);

        @Select("select * from `order` where user_id = #{userId} ")
        List<Order> findByUserId(Integer userId);

        @Select("select * from `order` where user_id = #{userId} and status = #{status} ")
        List<Order> findByUserIdAndStatus(@Param("userId") Integer userId, @Param("status") String status);

        @Insert("insert into order_logistics(product_id, order_id, tracking_number, status, created_at, updated_at)" +
                        "values (#{productId}, #{orderId}, #{trackingNumber} , 2, now(), now())")
        void createOrderLogistics(@Param("orderId") Integer orderId, @Param("productId") Integer productId,
                        @Param("trackingNumber") String trackingNumber);

        @Update("update order_logistics set tracking_number = #{trackingNumber}, updated_at = now() where order_id = #{orderId} and product_id = #{productId}")
        void updateOrderLogistics(Integer orderId, Integer productId, String trackingNumber);

        @Update("update order_logistics set status = 4, updated_at = now() where order_id = #{orderId}")
        void confirmOrder(Integer orderId);

        @Select("select * from `order` where id = #{orderId}")
        Order getDetailOfOrder(Integer orderId, Integer userId);

        @Select("select * from order_logistics where order_id = #{orderId}")
        List<LogisticsDTO> getLogistics(@Param("orderId") Integer orderId);

        @Select("select * from order_logistics where order_id = #{orderId} and product_id = #{productId}")
        LogisticsDTO getLogisticsByProductId(@Param("orderId") Integer orderId, @Param("productId") Integer productId);

        @Update("update order_logistics set status = 5, updated_at = now()")
        void cancelOrder(Integer orderId);

        @Select("SELECT order_item.order_id,order_item.product_id FROM product,store,order_item WHERE product.id=order_item.product_id AND store.id=product.store_id AND store.user_id=#{userId}")
        List<pro_order> getProducts(Integer userId);

        // 按订单查商品
        @Select("SELECT * FROM order_item WHERE order_id=#{orderId}")
        List<OrderItem> findProductsByOrder(Integer orderId);

        // 按商品查订单
        @Select("SELECT * FROM order_item WHERE product_id=#{productId}")
        List<OrderItem> findOrdersByProduct(Integer productId);

        // 仅更新状态
        @Update("update `order` set `status` = #{status}, `updated_at` = now() where `id`=#{orderId}")
        void updateOrderStatus(Integer orderId, Integer status);

        // 为orderitem更新状态
        @Update("update `order_item` set `status` = #{status}, `updated_at` = now() where `order_id`=#{orderId}")
        void updateOrderItemStatus(Integer orderId, Integer status);

        @Insert("INSERT INTO product_comment(user_id, product_id, content, rating, created_at) " +
                        "values (#{userId},#{productId},#{comment},#{rating},now())")
        void submitComment(Integer userId, Integer orderId, Integer productId, Integer rating, String comment);

        @Select("SELECT COUNT(*) FROM order_item WHERE spec = #{userId} AND product_id = #{productId}")
        int checkOrder(Integer userId, Integer productId);

}
