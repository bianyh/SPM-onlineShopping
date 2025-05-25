package com.example.spm.service;

import com.example.spm.pojo.Product;
import com.example.spm.pojo.ProductComment;
import com.example.spm.pojo.UserFavorites;

import java.util.List;

import com.example.spm.pojo.OrderItemRecordDTO;

public interface productService {
    Product getProductStockById(Integer productId);

    List<Product> getProducts(String keyword, int i, int limit);

    long getTotalCount(String keyword);

    Product getProductById(Integer id);

    int addProductToFavorite(UserFavorites userFavorites);

    List<ProductComment> getCommentsByProductId(Integer id);
    int getCommentsCountByProductId(Integer id);

    int submitComment(ProductComment productComment);

    int deleteComment(Integer cid);

    int removeProductFromFavorite(UserFavorites userFavorites);

    void updateProduct(Product product, Integer id);

    Integer addProduct(Product product);

    int updateProductStatus(int id, int status);

    int deleteProduct(Integer id);

    int getSalesVolume(Integer productId, int status);

    List<OrderItemRecordDTO> getSalesVolumeDetail(Integer productId, int status, String startTime, String endTime);

    List<Product> getProductsByRandom(int limit);
}

