package com.example.spm.controller;

import com.example.spm.pojo.*;
import com.example.spm.service.OrderService;
import com.example.spm.utils.JwtUtil;
import com.example.spm.utils.ThreadLocalUtil;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderservice;

    @PostMapping("")
    public Result submitOrder(@RequestHeader("Authorization") String authorization,
            @RequestBody OrderRequest orderRequest) {
        // 获取解析后的参数
        // Long addressId = orderRequest.getAddressId();
        // String paymentMethod = orderRequest.getPaymentMethod();
        // List<OrderItem> items = orderRequest.getItems();

        Map<String, Object> userInfo = JwtUtil.parseToken(authorization);

        orderservice.submitOrder(userInfo, orderRequest);

        return Result.success();
    }

    @GetMapping("/list")
    public Result ListOrders(
            @RequestParam(required = false) String status) {
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer userId = (Integer) map.get("id");
        List<Order> byUserIdAndStatus = orderservice.findByUserId(userId, status);
        return Result.success(byUserIdAndStatus);
    }

    /*
     * 对于status这个属性，0 未支付，1 已支付，2 待发货，3 运输中，4 已完成 ，5 已取消
     * 该接口是将订单的状态修改为代发货
     */
    @PutMapping("/update")
    public Result updateOrder(@Param("orderId") Integer orderId, @Param("productId") Integer productId) {
        orderservice.updateOrderLogistics(orderId, productId);
        return Result.success();
    }

    @PutMapping("/send")
    public Result sendOrder(@Param("orderId") Integer orderId, @Param("productId") Integer productId,
            @Param("trackingNumber") String trackingNumber) {
        System.out.println("orderId = " + orderId.toString());
        System.out.println("productId = " + productId.toString());
        System.out.println("trackingNumber = " + trackingNumber);
        orderservice.sendOrder(orderId, productId, trackingNumber);
        return Result.success();
    }

    @PutMapping("/confirm")
    public Result confirmOrder(Integer orderId) {
        orderservice.confirmOrder(orderId);
        return Result.success();
    }

    @PutMapping("/confirm/cancel")
    public Result cancelOrder(Integer orderId) {
        orderservice.cancelOrder(orderId);
        return Result.success();
    }

    @GetMapping("/detail")
    public Result getDetail(Integer orderId) {
        List<OrderItem> orderItem = orderservice.getOrderItems(orderId);
        return Result.success(orderItem);
    }

    @GetMapping("/logistics")
    public Result getLogistics(@RequestParam Integer orderId, @RequestParam(defaultValue = "-1") Integer productId) {
        if (productId == -1) {
            return Result.success(orderservice.getLogistics(orderId));
        }
        return Result.success(orderservice.getLogisticsByProductId(orderId, productId));
    }

    @GetMapping("/checkout")
    public Result checkout(Integer id) {
        BigDecimal totalAmount = orderservice.getDetail(id).getTotalAmount();
        return Result.success(totalAmount);
    }

    @GetMapping("/seller")
    public Result getOrder(@RequestHeader("Authorization") String authorization,
            Integer status) {
        Map<String, Object> userInfo = JwtUtil.parseToken(authorization);
        Integer userId = (Integer) userInfo.get("id");
        Map<Integer, List<Integer>> res = orderservice.getOrder(userId, status);
        return Result.success(res);
    }

    @GetMapping("/orderdetail")
    public Result getOrderDetail(Integer orderId) {
        return Result.success(orderservice.getDetail(orderId));
    }

    @GetMapping("/getItem")
    public Result getItem(Integer orderId, Long productId) {
        List<OrderItem> lists = orderservice.getOrderItems(orderId);
        List<OrderItem> list = new ArrayList<>();
        for (OrderItem orderItem : lists) {
            if (Objects.equals(orderItem.getProductId(), productId)) {
                list.add(orderItem);
            }
        }
        return Result.success(list);
    }

    @PutMapping("/status/{id}/{status}")
    public Result getProductComments(@PathVariable Integer id, @PathVariable Integer status) {
        orderservice.updateOrderStatus(id, status);
        return Result.success();
    }

    @PostMapping("/review")
    public Result UserComment(@RequestBody Comment comment) {
        Integer orderId = comment.getOrderId();
        Integer productId = comment.getProductId();
        Integer rating = comment.getRating();
        String com = comment.getComment();
        orderservice.submitComment(orderId, productId, rating, com);
        return Result.success("成功提交评价");
    }

    @GetMapping("/check/{productId}")
    public Result checkOrder(@PathVariable Integer productId) {
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer userId = (Integer) map.get("id");
        int result = orderservice.checkOrder(userId, productId);
        return Result.success(result);
    }

}
