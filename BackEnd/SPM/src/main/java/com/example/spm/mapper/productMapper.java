package com.example.spm.mapper;

import com.example.spm.pojo.Product;
import com.example.spm.pojo.ProductComment;
import com.example.spm.pojo.UserFavorites;
import org.apache.ibatis.annotations.*;

import java.math.BigDecimal;
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

    @Update("UPDATE product SET status = #{status} WHERE id = #{id}")
    int updataStatusById(int id, int status);

    @Delete("DELETE FROM product WHERE id = #{id}")
    int deleteProductById(Integer id);

    @Update("update product set " +
            "pictures = #{pictures}," +
            "price = #{price}," +
            "store_id = #{storeId}," +
            "name = #{name}," +
            "description = #{description}, " +
            "updated_at = now() " +
            "where id = #{id}")
    void updateProduct(String pictures, BigDecimal price, Integer storeId, String name, String description, Integer id);

    @Insert("insert into product(pictures, price, name, store_id, stock, description, created_at, updated_at)" +
            "values (#{pictures}, #{price}, #{name}, #{storeId}, #{stock}, #{description}, now(), now() )")
    Integer addProduct(@Param("pictures") String pictures, @Param("price") BigDecimal price, @Param("name") String name
            , @Param("storeId") Integer storeId, @Param("stock") Integer stock, @Param("description") String description);
}
