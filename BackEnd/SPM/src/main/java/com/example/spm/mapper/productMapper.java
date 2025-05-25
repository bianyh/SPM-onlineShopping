package com.example.spm.mapper;

import com.example.spm.pojo.Product;
import com.example.spm.pojo.ProductComment;
import com.example.spm.pojo.UserFavorites;

import org.apache.ibatis.annotations.*;

import java.math.BigDecimal;
import java.util.List;

import com.example.spm.pojo.OrderItemRecordDTO;

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

        @Select("SELECT COUNT(*) FROM product_comment WHERE product_id = #{id}")
        int getCommentsCountByProductId(Integer id);

        @Insert("INSERT INTO product_comment (user_id, product_id, content, rating, created_at, status) VALUES (#{userId}, #{productId}, #{content}, #{rating}, NOW(), 1)")
        int submitComment(ProductComment productComment);

        @Delete("DELETE FROM product_comment WHERE id = #{cid}")
        int deleteComment(Integer cid);

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
                        "updated_at = now(), " +
                        "stock = #{stock}, " +
                        "status = #{status} " +
                        "where id = #{id}")
        void updateProduct(String pictures, BigDecimal price, Integer storeId, String name, String description,
                        Integer id, Integer stock, Integer status);

        @Insert("insert into product(pictures, price, name, store_id, stock, description, created_at, updated_at)" +
                        "values (#{pictures}, #{price}, #{name}, #{storeId}, #{stock}, #{description}, now(), now() )")
        Integer addProduct(@Param("pictures") String pictures, @Param("price") BigDecimal price,
                        @Param("name") String name, @Param("storeId") Integer storeId, @Param("stock") Integer stock,
                        @Param("description") String description);

        @Select("select * from product where store_id = #{storeId} ")
        List<Product> getProductByStoreId(Integer storeId);

        @Select("select * from store where id in #{storeIds} ")
        List<Product> findByShopIdIn(List<Integer> shopIds);

        @Select("SELECT COALESCE(SUM(quantity), 0) AS total_quantity FROM order_item WHERE product_id = #{productId} AND status = #{status}")
        int getSalesVolume(Integer productId, int status);

        @Select("SELECT COALESCE(SUM(quantity), 0) AS total_quantity FROM order_item WHERE product_id = #{productId}")
        int getSalesVolumeAll(Integer productId);

        @Select("SELECT order_item.order_id, order_item.quantity, order_item.price, order_item.spec, order_item.status, order_item.created_at, order_item.updated_at "
                        +
                        "FROM order_item " +
                        "WHERE order_item.product_id = #{productId} " +
                        "AND order_item.created_at BETWEEN #{startDate} AND #{endDate} " +
                        "AND order_item.status = #{status}")
        List<OrderItemRecordDTO> getSalesVolumeDetail(
                        @Param("productId") Integer productId,
                        @Param("status") Integer status,
                        @Param("startDate") String startDate,
                        @Param("endDate") String endDate);

        @Select("SELECT * FROM product ORDER BY RAND() LIMIT #{limit}")
        List<Product> getProductsByRandom(int limit);
}
