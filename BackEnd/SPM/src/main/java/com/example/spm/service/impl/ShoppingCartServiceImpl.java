package com.example.spm.service.impl;

import com.example.spm.mapper.ShoppingCartItemMapper;
import com.example.spm.pojo.ShoppingCartItem;
import com.example.spm.service.ShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {
    @Autowired
    private ShoppingCartItemMapper shoppingCartItemMapper;

    @Override
    public  List<ShoppingCartItem> getCartItems(long userId) {
        return shoppingCartItemMapper.findAllCartItems(userId);
    }

    @Override
    public Integer getTotalQuantity(Integer userId) {
        return shoppingCartItemMapper.sumQuantityByUserId(Long.valueOf(userId));
    }

    @Override
    public int addProductToCart(ShoppingCartItem shoppingCartItem) {
        Integer result=shoppingCartItemMapper.findCartItemByUserIdAndProductId(shoppingCartItem.getUserId(),shoppingCartItem.getProductId());
        if(result==null){
            return shoppingCartItemMapper.insert(shoppingCartItem);
        }else{
            long product_id=shoppingCartItem.getProductId();
            return shoppingCartItemMapper.updateCartItem(shoppingCartItem,shoppingCartItem.getUserId(),product_id);
        }
    }



    @Override
    public int updateCartItem(ShoppingCartItem shoppingCartItem, Integer userId) {
        long productId=shoppingCartItem.getProductId();
        return shoppingCartItemMapper.updateCartItem(shoppingCartItem, userId, productId);
    }

    @Override
    public Integer deleteCart(Integer cartId, Integer userId) {
        return shoppingCartItemMapper.deleteCartItem(cartId, userId);
    }

}
