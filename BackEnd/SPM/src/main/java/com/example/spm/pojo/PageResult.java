package com.example.spm.pojo;

import lombok.Data;

import java.util.List;

@Data
public class PageResult<T> {
    private List<T> data;
    private int total;
    private int page;
    private int pageSize;

    public PageResult(List<T> stores, int total, int page, int pageSize) {
        this.data = stores;
        this.total = total;
        this.page = page;
        this.pageSize = pageSize;
    }
}