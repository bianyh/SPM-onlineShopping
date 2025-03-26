package com.example.spm.service.impl;

import com.example.spm.enums.OrderStatus;
import com.example.spm.mapper.OrderMapper;
import com.example.spm.mapper.productMapper;
import com.example.spm.mapper.sellerMapper;
import com.example.spm.pojo.*;
import com.example.spm.service.OrderService;
import com.example.spm.utils.ThreadLocalUtil;
import io.micrometer.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.mapping;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private productMapper productmapper;

    @Autowired
    private sellerMapper sellerMapper;

    @Override
    @Transactional
    public void submitOrder(Map<String, Object> userInfo, OrderRequest orderRequest) {
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
        Order order = new Order();
        order.setUserId(userId);
        order.setAddressId(addressId);
        order.setTotalAmount(totalAmount);
        order.setPaymentMethod(paymentMethod);
        order.setStatus(0);
        order.setCreatedAt(LocalDateTime.now());
        order.setUpdatedAt(LocalDateTime.now());

        orderMapper.submitOrder(order);

        for (OrderItem i : items) {
            Product pro = productmapper.getProductById(Math.toIntExact(i.getProductId()));
            orderMapper.submitOrderItem(order.getId(), i.getProductId(), i.getQuantity(), pro.getPrice().multiply(BigDecimal.valueOf(i.getQuantity())), i.getSpec());
        }
    }

    @Override
    public List<Order> findByUserId(Integer userId, String status) {
        if (StringUtils.isNotBlank(status)) {
            return orderMapper.findByUserIdAndStatus(userId, status);
        }
        return orderMapper.findByUserId(userId);
    }

    @Override
    public MerchantOrderResponse findOrdersOfSeller(Integer status) {
        return null;
    }

//    @Override
//    public MerchantOrderResponse findOrdersOfSeller(Integer status) {
//        Map<String, Object> map = ThreadLocalUtil.get();
//        Integer userId = (Integer) map.get("id");
//
//        // 1. 获取商家所有店铺
//        List<Store> stores = com.example.spm.mapper.sellerMapper.findByUserId(userId);
//
//        // 2. 获取店铺下所有商品ID
//        List<Integer> shopIds = stores.stream().map(Store::getId).collect(toList());
//        List<Product> products = productmapper.findByShopIdIn(shopIds);
//
//        // 3. 获取符合条件的订单项
//        List<OrderItem> orderItems = orderMapper.findByProductIdsAndStatus(
//                products.stream().map(Product::getId).collect(toList()),
//                OrderStatus.fromCode(status)
//        );
//
//        // 4. 构建层次化数据结构
//        return buildHierarchicalResponse(stores, products, orderItems);
//    }
//
//    private MerchantOrderResponse buildHierarchicalResponse(
//            List<Store> shops,
//            List<Product> products,
//            List<OrderItem> orderItems
//    ) {
//        // 使用Stream API进行三级嵌套分组
//        Map<Store, Map<Product, List<MerchantOrderResponse.OrderItemDTO>>> groupedData = orderItems.stream()
//                .collect(groupingBy(
//                        item -> findShopByProduct(shops, products, item.getProductId()),
//                        groupingBy(
//                                item -> findProductById(products, item.getProductId()),
//                                mapping(this::convertToDTO, toList())
//                        )
//                ));
//
//        // 转换为DTO对象树
//        return new MerchantOrderResponse(groupedData);
//    }
//
//    // 定义 convertToDTO 方法
//    private MerchantOrderResponse.OrderItemDTO convertToDTO(OrderItem item) {
//        return new MerchantOrderResponse.OrderItemDTO(
//
//                item.getProductId(),
//                item.getQuantity(),
//                item.getSpec()
//        );
//    }
//
//    // 辅助方法：通过商品ID查找店铺
//    private Store findShopByProduct(List<Store> shops, List<Product> products, Long productId) {
//        Optional<Product> product = products.stream().filter(p -> p.getId().equals(productId)).findFirst();
//        if (product.isPresent()) {
//            Integer shopId = product.get().getId();
//            return shops.stream().filter(s -> s.getId() == (shopId)).findFirst().orElse(null);
//        }
//        return null;
//    }
//
//    // 辅助方法：通过商品ID查找商品
//    private Product findProductById(List<Product> products, Long productId) {
//        return products.stream().filter(p -> p.getId().equals(productId)).findFirst().orElse(null);
//    }
}
