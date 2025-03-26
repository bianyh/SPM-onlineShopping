package com.example.spm.service.impl;

import com.example.spm.mapper.sellerMapper;
import com.example.spm.mapper.userMapper;
import com.example.spm.pojo.PageResult;
import com.example.spm.pojo.Store;
import com.example.spm.service.sellerService;
import com.example.spm.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

@Service
public class sellerServiceImpl implements sellerService {

    @Autowired
    private sellerMapper mapper;
    @Autowired
    private userMapper userMapper;

    private static final int PAGE_SIZE = 10;

    @Override
    public Store findByName(String name) {
        Store store = mapper.findByName(name);
        return store;
    }

    @Override
    public void addStore(Store store) {
        Map<String,Object> map = ThreadLocalUtil.get();
        Integer userId = (Integer) map.get("id");
        store.setStatus(2);
        store.setUserId(userId);
        mapper.addStore(store.getName(), userId, store.getAddress() );
    }

    @Override
    public void updateStore(Store store) {
        mapper.updateStore(store.getName(),store.getDescription(),store.getPicturePath(),store.getStatus(),store.getId());
    }

    @Override
    public void deleteStore(Integer id) {
        mapper.deleteStore(id);
    }

    @Override
    public List<Store> findByUserName(String username) {
        Integer userId = userMapper.findByUsername(username).getId();
        return mapper.findByUserName(userId);
    }

    @Override
    public Store findById(Integer id) {
        return mapper.findById(id);
    }

    @Override
    public PageResult<Store> searchStoresByName(String name, int page) {
        // 计算分页偏移量
        int offset = page * PAGE_SIZE;

        // 查询数据库
        List<Store> stores = mapper.findByNameContaining(name, offset, PAGE_SIZE);
        int total = mapper.countByNameContaining(name);

        System.out.println(stores);
        System.out.println(total);

        // 返回分页结果
        return new PageResult<>(stores, total, page, PAGE_SIZE);
    }

    public List<Store> findByUserId(Integer userId) {
        return mapper.findByUserName(userId);
    }

    private Store convertToVO(Store store) {
        Store vo = new Store();
        vo.setId(store.getId());
        vo.setName(store.getName());
        vo.setDescription(store.getDescription());
        vo.setPicturePath(store.getPicturePath());
        vo.setStatus(store.getStatus());
        return vo;
    }
}
