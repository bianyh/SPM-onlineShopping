package com.example.spm.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;


@Data
public class ShoppingCartItem {
    // 购物车项的唯一标识符
    private Long id;
    // 用户ID，标识购物车项属于哪个用户
    private Integer userId;
    // 产品ID，标识购物车项中的产品
    private Long productId;
    // 产品数量，表示用户选择的该产品的数量
    private int quantity;
    // 产品规格，用于记录用户选择的产品规格信息
    private String spec;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")

    // 创建时间，记录购物车项的创建时间
    private Date createdAt;
}