package com.example.spm.service;

import com.example.spm.pojo.Product;
import com.example.spm.pojo.ProductComment;
import com.example.spm.pojo.UserFavorites;

import java.util.List;

public interface productService {
    Product getProductStockById(Integer productId);

    List<Product> getProducts(String keyword, int i, int limit);

    long getTotalCount(String keyword);

    Product getProductById(Integer id);

    int addProductToFavorite(UserFavorites userFavorites);

    List<ProductComment> getCommentsByProductId(Integer id);

    int submitComment(ProductComment productComment);

    int removeProductFromFavorite(UserFavorites userFavorites);
}
