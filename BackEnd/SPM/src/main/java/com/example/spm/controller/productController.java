package com.example.spm.controller;

import com.example.spm.pojo.ProductComment;
import com.example.spm.pojo.UserFavorites;
import com.example.spm.service.productService;
import com.example.spm.pojo.Result;
import com.example.spm.pojo.Product;
import com.example.spm.utils.ThreadLocalUtil;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.spm.pojo.OrderItemRecordDTO;
import com.example.spm.service.OrderService;

@RestController
@RequestMapping("/product")
public class productController {
    @Autowired
    private productService productservice;

    @GetMapping
    public Result getProducts(
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int limit) {

        List<Product> products = productservice.getProducts(keyword, page-1, limit);
        long total = productservice.getTotalCount(keyword);

        Map<String, Object> data = Map.of(
                "page", page,
                "limit", limit,
                "total", total,
                "products", products
        );

        return Result.success(data);
    }
    //    @PostMapping
//    public int addProduct(@RequestBody Product product) {
//        return productService.addProduct(product);
//    }
//
    @GetMapping("/search/{id}")
    public Result getProductById(@PathVariable Integer id) {
        return Result.success(productservice.getProductById(id));
    }
    @GetMapping("/stock/{productId}")
    public Result getProductStock(@PathVariable Integer productId) {

        Product product = productservice.getProductStockById(productId);
        if (product != null) {

            Map<String, Object> data = new HashMap<>();
            data.put("stock", product.getStock());

            return Result.success(data);
        } else {

            return  Result.error("未找到该商品");
        }
    }
    @PostMapping("/favorite")
    public Result addProductToFavorite(@RequestBody UserFavorites userFavorites) {
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer userId = (Integer) map.get("id");
        if (userId != null) {
            userFavorites.setUserId(userId);
        } else {
            return Result.error("获取用户ID失败");
        }

        Map<String, Object> result = new HashMap<>();
        int rowsAffected = productservice.addProductToFavorite(userFavorites);
        if (rowsAffected > 0) {
            result.put("code", 0);
            result.put("message", "商品添加到收藏夹成功");
            return Result.success(result);
        } else {
            return Result.error("添加商品到收藏夹失败");
        }
    }
    @DeleteMapping("/favorite")
    public Result removeProductFromFavorite(@RequestBody UserFavorites userFavorites) {
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer userId = (Integer) map.get("id");
        if (userId != null) {
            userFavorites.setUserId(userId);
        } else {
        return Result.error("获取用户ID失败");
        }

        Map<String, Object> result = new HashMap<>();
        int rowsAffected = productservice.removeProductFromFavorite(userFavorites);
        if (rowsAffected > 0) {
            result.put("code", 0);
            result.put("message", "商品从收藏夹中移除成功");
            return Result.success(result);
        } else {

            return Result.error("从收藏夹中移除商品失败");
        }
    }
    
    @GetMapping("/{id}/reviews")
    public Result getProductComments(@PathVariable Integer id) {
        List<ProductComment> comments = productservice.getCommentsByProductId(id);
        if (comments != null) {
            return Result.success(comments);
        } else {
            return Result.error("未找到该商品的评论");
        }
    }

    @GetMapping("/{id}/reviews/count")
    public Result getProductCommentsCount(@PathVariable Integer id) {
        int comments = productservice.getCommentsCountByProductId(id);
        return Result.success(comments);
    }

    @PostMapping("/{id}/reviews")
    public Result submitComment(@PathVariable Integer id, @RequestBody ProductComment productComment) {
        productComment.setProductId(id);
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer userId = (Integer) map.get("id");
        if (userId != null) {
            productComment.setUserId(userId);
        } else {
            return Result.error("获取用户ID失败");
        }

        Map<String, Object> result = new HashMap<>();
        int rowsAffected = productservice.submitComment(productComment);
        if (rowsAffected > 0) {
            result.put("code", 0);
            result.put("message", "评论提交成功");
            return Result.success(result);
        } else {

            return Result.error("评论提交失败");
        }
    }
    
    @DeleteMapping("/{productId}/reviews/{commentId}")
    public Result submitComment(@PathVariable Integer productId, @PathVariable Integer commentId) {
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer userId = (Integer) map.get("id");
        if (userId == null) {
            return Result.error("获取用户ID失败");
        }
        Map<String, Object> result = new HashMap<>();
        int rowsAffected = productservice.deleteComment(commentId);
        if (rowsAffected > 0) {
            //result.put("code", 0);
            result.put("message", "评论删除成功");
            return Result.success(result);
        } else {

            return Result.error("评论删除失败");
        }
    }

    @PutMapping("/status")
    public Result updateProductStatus(
            @RequestHeader("Authorization") String authorization,
            @RequestBody Map<String, String> requestBody) {

        String id = requestBody.get("id");
        String status = requestBody.get("status");
        int statusNum = 0;

        if (status.equals("上架中")) {
            statusNum = 0;
        } else if (status.equals("已上架")) {
            statusNum = 1;
        } else {
            statusNum = 2;
        }
        Map<String, Object> result = new HashMap<>();
        int rowsAffected = productservice.updateProductStatus(Integer.valueOf(id), statusNum);
        if (rowsAffected > 0) {
            result.put("code", 0);
            result.put("message", "商品状态修改成功");
            return Result.success(result);
        } else {

            return Result.error("商品状态修改失败");
        }
    }
    @DeleteMapping("/delete/{id}")
    public Result deleteProduct(
            @RequestHeader("Authorization") String authorization,
            @PathVariable Integer id) {

        Map<String, Object> result = new HashMap<>();
        int rowsAffected = productservice.deleteProduct(id);
        if (rowsAffected > 0) {
            result.put("code", 0);
            result.put("message", "商品删除成功");
            return Result.success(result);
        } else {

            return Result.error("商品删除失败");
        }
    }

    //获取某一商品的销量（数字）
    @GetMapping("/{id}/sales")
    public Result getSalesVolume(@PathVariable Integer id, @RequestParam(defaultValue = "-1") int status) {
        if (status < -1) {
            return Result.error("状态参数错误");
        }
        int salesVolume = productservice.getSalesVolume(id, status);
        if (salesVolume < 0) {
            return Result.error("销量查询失败");
        }
        if (salesVolume > 0) {
            return Result.success(salesVolume);
        } else {
            return Result.error("销量查询失败");
        }
    }

    //获取某一商品的详细销量信息
    @GetMapping("/{productId}/sales/detail")
    public Result getSalesVolumeDetail(@PathVariable Integer productId, @RequestParam(defaultValue = "-1") int status, 
            @RequestParam(defaultValue = "1970-01-01 00:00:00") String startTime,
            @RequestParam(defaultValue = "9999-12-31 23:59:59") String endTime) {
        if (status < 0) {
            status = 0;
        }
        List<OrderItemRecordDTO> result = productservice.getSalesVolumeDetail(productId, status, startTime, endTime);
        return Result.success(result);
    }
    
    //随机选取数个商品
    @GetMapping("/random")
    public Result getRandomProducts(@RequestParam(defaultValue = "5") int limit) {
        List<Product> products = productservice.getProductsByRandom(limit);
        return Result.success(products);
    }

}
