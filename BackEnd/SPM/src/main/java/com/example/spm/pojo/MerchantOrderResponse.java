package com.example.spm.pojo;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class MerchantOrderResponse {
    private List<ShopDTO> shops;

    public MerchantOrderResponse(Map<Store, Map<Product, List<OrderItemDTO>>> groupedData) {
    }

    @Data
    public static class ShopDTO {
        private Long id;
        private String name;
        private List<ProductDTO> products;
    }

    @Data
    public static class ProductDTO {
        private Long id;
        private String title;
        private List<OrderItemDTO> orderItems;
    }

    @Data
    public static class OrderItemDTO {
        private Long productId;
        private Integer quantity;
        private String spec;

        public OrderItemDTO(Long productId, int quantity, String spec) {
            this.productId = productId;
            this.quantity = quantity;
            this.spec = spec;
        }
    }
}
