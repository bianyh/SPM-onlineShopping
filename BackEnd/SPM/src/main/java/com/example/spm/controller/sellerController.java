package com.example.spm.controller;

import com.example.spm.pojo.PageResult;
import com.example.spm.pojo.Product;
import com.example.spm.pojo.Result;
import com.example.spm.pojo.Store;
import com.example.spm.service.sellerService;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.Period;
import java.util.List;

@RestController
@RequestMapping("/seller")
public class sellerController {

    @Autowired
    private sellerService sellerService;
    @Autowired
    private com.example.spm.service.productService productService;

    @PostMapping("/register")
    public Result registerStore(@RequestBody Store store){
        if(sellerService.findByName(store.getName()) != null){
            return Result.error("该店铺名已被使用！");
        }
        sellerService.addStore(store);
        return Result.success();
    }

    @PutMapping("/update")
    public Result updateStore(@RequestBody Store store){
        sellerService.updateStore(store);
        return Result.success();
    }

    @DeleteMapping("/withdraw/{id}")
    public Result deleteStore(@PathVariable("id") Integer id){
        sellerService.deleteStore(id);
        return Result.success();
    }

    @GetMapping("/all/{username}")
    public Result getStoreList(@PathVariable("username") String username){
        List<Store> stores = sellerService.findByUserName(username);
        return Result.success(stores);
    }

    @GetMapping("/get/{id}")
    public Result getStoreById(@PathVariable("id") Integer id){
        Store store = sellerService.findById(id);
        return Result.success(store);
    }

    @GetMapping("/query/{name}")
    public Result getStoreByName(@PathVariable("name") String name,@RequestParam(defaultValue = "0") @Min(0) int page){

        if (name == null || name.trim().isEmpty()){
            return Result.error("店铺名称不能为空!");
        }
        PageResult<Store> result = sellerService.searchStoresByName(name, page);
        return Result.success(result);
    }

    /*
    * 更新已有商品的信息，若商品不存在，则新建商品。传回商品唯一id*/
    @PutMapping("/{id}/update")
    public Result updateProduct(@PathVariable("id") Integer id,@RequestBody Product product){
        if (productService.getProductById(id) == null) {
            Integer id1 = productService.addProduct(product);
            return Result.success("添加商品成功, id为：" + id1);
        }

        productService.updateProduct(product, id);
        return Result.success("成功更新商品信息");
    }

    @GetMapping("/products")
    public Result getAllProducts(Integer id, Integer status,Integer page,Integer limit){
        PageResult<Product> pp = sellerService.findAllProducts(id, status, page, limit);
        return Result.success(pp);
    }

}
