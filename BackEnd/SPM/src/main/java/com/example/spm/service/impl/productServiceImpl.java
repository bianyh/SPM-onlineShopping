package com.example.spm.service.impl;

import com.example.spm.mapper.productMapper;
import com.example.spm.pojo.Product;
import com.example.spm.pojo.ProductComment;
import com.example.spm.pojo.UserFavorites;
import com.example.spm.service.productService;
import com.example.spm.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class productServiceImpl implements productService {
    @Autowired
    private productMapper productmapper;

    @Override
    public Product getProductById(Integer id) {
        return productmapper.getProductById(id);
    }

    @Override
    public int addProductToFavorite(UserFavorites userFavorites) {
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer userId = (Integer) map.get("id");
        userFavorites.setUserId(userId);
        userFavorites.setCreatedAt(new Date());
        return productmapper.addProductToFavorite(userFavorites);
    }

    @Override
    public List<ProductComment> getCommentsByProductId(Integer id) {
        return productmapper.getCommentsByProductId(id);
    }

    @Override
    public int submitComment(ProductComment productComment) {
        productComment.setCreatedAt(new Date());
        productComment.setStatus(1);
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer userId = (Integer) map.get("id");
        productComment.setUserId(userId);
        return productmapper.submitComment(productComment);
    }

    @Override
    public int removeProductFromFavorite(UserFavorites userFavorites) {
        return productmapper.removeProductFromFavorite(userFavorites);
    }

    @Override
    public void updateProduct(Product product, Integer id) {
        productmapper.updateProduct(product.getPictures(),product.getPrice(),product.getStoreId(),product.getName(),
                product.getDescription(), id);
    }

    @Override
    public Integer addProduct(Product product) {
         return productmapper.addProduct(product.getPictures(),product.getPrice(),product.getName(),product.getStoreId()
                 ,product.getStock(), product.getDescription());
    }

    @Override
    public Product getProductStockById(Integer productId) {
        return productmapper.getProductStockById(productId);
    }

    @Override
    public List<Product> getProducts(String keyword, int page, int limit) {
        return  productmapper.getProducts(keyword, page, limit);
    }

    @Override
    public long getTotalCount(String keyword) {
        return productmapper.getTotalCount(keyword);
    }


    @Override
    public int updateProductStatus(int id, int status) {
        return productmapper.updataStatusById(id, status);
    }

    @Override
    public int deleteProduct(Integer id) {
        return productmapper.deleteProductById(id);
    }
}
