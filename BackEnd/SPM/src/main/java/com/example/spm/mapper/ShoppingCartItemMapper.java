package com.example.spm.mapper;
import com.example.spm.pojo.ShoppingCartItem;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ShoppingCartItemMapper {
    @Select("SELECT * FROM cart WHERE user_id = #{userId}")
    Integer sumQuantityByUserId(Long userId);
    @Insert("INSERT INTO cart (user_id, product_id, quantity, spec, created_at) VALUES (#{userId}, #{productId}, #{quantity}, #{spec}, #{createdAt})")
    int insert(ShoppingCartItem shoppingCartItem);
    @Select("SELECT * FROM cart WHERE user_id = #{userId}")
    List<ShoppingCartItem> findAllCartItems(long userId);

    @Update("UPDATE cart SET quantity = #{shoppingCartItem.quantity}, spec = #{shoppingCartItem.spec}, created_at = now() WHERE user_id = #{userId} AND product_id = #{productId}")
    int updateCartItem(ShoppingCartItem shoppingCartItem, Integer userId, Long productId);
    @Select("SELECT * FROM cart WHERE user_id = #{userId} AND product_id = #{productId}")
    Integer findCartItemByUserIdAndProductId(Integer userId, Long productId);
    @Delete("DELETE FROM cart WHERE product_id = #{cartId} AND user_id = #{userId}")
    Integer deleteCartItem(Integer cartId, Integer userId);
}
