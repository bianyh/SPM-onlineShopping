package com.example.spm.pojo;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class PaymentParam {

    /**
     * 跳转路径
     */
    private String returnUrl;

    /**
     * 订单号
     */
    private String outTradeNo;

    /**
     * 支付金额
     */
    private BigDecimal totalAmount;

    /**
     * 商品名称
     */
    private String subject;

    /**
     * 支付类型 手机：QUICK_WAP_WAY 电脑：FAST_INSTANT_TRADE_PAY
     */
    private String productCode;



}
