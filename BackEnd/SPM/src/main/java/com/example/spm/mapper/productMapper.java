package com.example.spm.mapper;

import com.example.spm.pojo.Product;
import com.example.spm.pojo.ProductComment;
import com.example.spm.pojo.UserFavorites;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface productMapper {
    @Select("SELECT * FROM product WHERE id = #{id}")
    Product getProductById(Integer id);

    @Select("SELECT * FROM product WHERE name LIKE CONCAT('%', #{keyword}, '%') LIMIT #{page}, #{limit}")
    List<Product> getProducts(String keyword, int page, int limit);
    @Select("SELECT COUNT(*) FROM product WHERE name LIKE CONCAT('%', #{keyword}, '%')")
    long getTotalCount(String keyword);
    @Select("SELECT * FROM product WHERE id = #{productId}")
    Product getProductStockById(Integer productId);
    @Insert("INSERT INTO user_favorites (user_id, product_id, created_at) VALUES (#{userId}, #{productId}, NOW())")
    int addProductToFavorite(UserFavorites userFavorites);
    @Select("SELECT * FROM product_comment WHERE  product_id = #{id}")
    List<ProductComment> getCommentsByProductId(Integer id);
    @Insert("INSERT INTO product_comment (user_id, product_id, content, rating, created_at, status) VALUES (#{userId}, #{productId}, #{content}, #{rating}, NOW(), 1)")
    int submitComment(ProductComment productComment);
    @Delete("DELETE FROM user_favorites WHERE user_id = #{userId} AND product_id = #{productId}")
    int removeProductFromFavorite(UserFavorites userFavorites);
}
