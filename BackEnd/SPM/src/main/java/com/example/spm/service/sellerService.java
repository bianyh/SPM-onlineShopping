package com.example.spm.service;

import com.example.spm.pojo.PageResult;
import com.example.spm.pojo.Store;
import jakarta.validation.constraints.Min;

import java.util.List;

public interface sellerService {
    Store findByName(String name);

    void addStore(Store store);

    void updateStore(Store store);

    void deleteStore(Integer id);

    List<Store> findByUserName(String username);

    Store findById(Integer id);

    PageResult<Store> searchStoresByName(String name, @Min(0) int page);
}
