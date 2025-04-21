package com.example.spm.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.spm.mapper.VerificationMapper;
import com.example.spm.pojo.Verification;
import com.example.spm.pojo.VerificationDTO;
import com.example.spm.service.VerificationService;

@Service
public class VerificationServiceImpl implements VerificationService {

    @Autowired
    private VerificationMapper vm;

    @Override
    public int add(Verification v) {
        return vm.add(v.getType(), v.getUserId(), v.getStatus(), v.getContent());
    }

    @Override
    public List<Verification> search(VerificationDTO vdto) {
        String userId = String.valueOf(vdto.getUserId());
        String status = String.valueOf(vdto.getStatus());
        String reviewerId = String.valueOf(vdto.getReviewerId());
        String type = String.valueOf(vdto.getType());
        int page = vdto.getPage();
        int limit = vdto.getLimit();
        if ("-1".equals(reviewerId))
            reviewerId = "%";
        if ("-1".equals(userId))
            userId = "%";
        if ("-1".equals(status))
            status = "%";
        if ("-1".equals(type))
            type = "%";
        return vm.search(userId, status, reviewerId, type, page, limit);
    }

    //改
    @Override
    public void updateById(Integer vid) {

        throw new UnsupportedOperationException("Not supported yet.");
    }

    //结束
    @Override
    public void settleById(Integer vid) {
        
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Verification findById(Integer vid) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<Verification> findByUserId(Integer userId) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void deleteById(Integer vid) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
