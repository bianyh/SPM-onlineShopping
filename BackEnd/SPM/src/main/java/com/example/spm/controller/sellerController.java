package com.example.spm.controller;

import com.example.spm.pojo.OrderItemRecordDTO;
import com.example.spm.pojo.PageResult;
import com.example.spm.pojo.Product;
import com.example.spm.pojo.Result;
import com.example.spm.pojo.Store;
import com.example.spm.service.sellerService;
import com.example.spm.utils.ThreadLocalUtil;

import jakarta.validation.constraints.Min;
import lombok.Data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.example.spm.service.productService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/seller")
public class sellerController {

    @Autowired
    private sellerService sellerService;
    @Autowired
    private productService productService;
    
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

    //获取店铺的总体销量
    @GetMapping("/sales")
    public Result getSalesVolumeTotal() {
        Map<String, Object> userInfo = ThreadLocalUtil.get();
        Integer userId = (Integer) userInfo.get("id");
        if (userId == null) {
            return Result.error("获取用户ID失败");
        }
        List<SalesVolumeDTO> salesVolumeList = new ArrayList<>();

        //从id获取用户店铺
        List<Store> stores = sellerService.findByUserId(userId);
        if (stores.isEmpty()) {
            return Result.error("用户没有店铺");
        }
        for (Store store : stores) {
            System.out.println(store.getId());
            Integer localSalesVolume = 0;
            //获取店铺的商品
            List<Product> products = sellerService.findAllProducts(store.getId(), null, 1, 1000).getData();
            for (Product product : products) {
                //获取商品的销量
                localSalesVolume += productService.getSalesVolume(product.getId(), -1);
            }
            salesVolumeList.add(new SalesVolumeDTO(store.getId(), store.getName(), localSalesVolume));
        }
        return Result.success(salesVolumeList);
    }

    //销量数据传输对象
    public class SalesVolumeDTO {
        public Integer storeId;
        public String storeName;
        public Integer salesVolume;
        
        public SalesVolumeDTO(Integer storeId, String storeName, Integer salesVolume) {
            this.storeId = storeId;
            this.storeName = storeName;
            this.salesVolume = salesVolume;
        }
    }

    @GetMapping("/sales/{storeId}")
    public Result getSalesVolumeByStoreId(@PathVariable("storeId") Integer storeId) {
        List<Product> products = sellerService.findAllProducts(storeId, null, 1, 1000).getData();
        
        List<StoreSalesVolumeDTO> salesVolumeList = new ArrayList<>();
        for (Product product : products) {
            StoreSalesVolumeDTO storeSalesVolume = new StoreSalesVolumeDTO(product.getId(), product.getName());
            storeSalesVolume.setAchieveVolume(productService.getSalesVolume(product.getId(), 5));
            storeSalesVolume.setOrderVolume(productService.getSalesVolume(product.getId(), 0) + productService.getSalesVolume(product.getId(), 1) + productService.getSalesVolume(product.getId(), 2));
            storeSalesVolume.setCancelVolume(productService.getSalesVolume(product.getId(), 4));
            storeSalesVolume.setSalesVolume(productService.getSalesVolume(product.getId(), -1));
            salesVolumeList.add(storeSalesVolume);
        }
        return Result.success(salesVolumeList);
    }

    @Data
    public class StoreSalesVolumeDTO {
        private Integer productId;
        private String productName;
        private Integer salesVolume;
        private Integer achieveVolume;
        private Integer orderVolume;
        private Integer cancelVolume;

        public StoreSalesVolumeDTO(Integer productId, String productName) {
            this.productId = productId;
            this.productName = productName;
        }
    }
}
