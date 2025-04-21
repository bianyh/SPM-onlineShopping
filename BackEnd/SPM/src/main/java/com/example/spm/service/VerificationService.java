package com.example.spm.service;

import java.util.List;

import com.example.spm.pojo.Verification;
import com.example.spm.pojo.VerificationDTO;

public interface VerificationService {
    
    Verification findById(Integer vid);

    List<Verification> findByUserId(Integer userId);

    List<Verification> search(VerificationDTO vdto);

    void deleteById(Integer vid);

    //改
    void updateById(Integer vid);

    //结束
    void settleById(Integer vid);

    //加
    int add(Verification v);
}
