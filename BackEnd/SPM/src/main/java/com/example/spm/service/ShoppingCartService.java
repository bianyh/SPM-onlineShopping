package com.example.spm.service;

import com.example.spm.pojo.ShoppingCartItem;

import java.util.List;

public interface ShoppingCartService {
    List<ShoppingCartItem> getCartItems(long userId) ;

    Integer getTotalQuantity(Integer userId);

    int addProductToCart(ShoppingCartItem shoppingCartItem);

    int updateCartItem(ShoppingCartItem shoppingCartItem, Integer userId);
}
