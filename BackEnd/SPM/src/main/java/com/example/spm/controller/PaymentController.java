package com.example.spm.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.response.AlipayTradePagePayResponse;
import com.example.spm.pojo.PaymentParam;
import com.example.spm.pojo.Result;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 支付接口
 */
@Controller
@Api(tags = "PaymentController", description = "支付接口")
@RequestMapping("/aliyun/pay")
public class PaymentController {

    //appid
    private final String APP_ID = "2021000148631960";
    //应用私钥
    private final String APP_PRIVATE_KEY = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCpSycKR9SQwGLaOaIOap5xV88tgHTSriosNhPwaKOibuXBBeMVBiNvpnNmtFBwH1Nx67seZOi6epkqaHPa3fGsEbGZHHQeDEtsCVZd15/rfV0LlZS7xUvATBH63ThgezrC+WU3i4WZUqzwB5Rn8nYkEo7UFyi6/1rkz6RRwvLqxVUyTGbieu40iIMlcwvPLD0TC0B5fERdCri9MU14lEnWHD75DHwQ/KaYBXDFe4MFr/Bwdkk2Lh4AJITrbmDfspNmzOpVoTiDqKNpy2T18kxJdmlPqhbu09DIps1mK1wRxpD8hHAHhgJnN3iuBoFhjdXbC3Cbrp1Qq3Meaq+9ApobAgMBAAECggEAFEnLXfIJyWck8Z4ii7O/VTn+vCc+2NjCeRXqAc+ofJjps94uvqBDsuexwMcvNKCscWScwiG5uk9i97AY4kvi4IomTIEacVdr2oCr9wcWRTR66B5++pnEYEE9jmDglr9t5VvaA3QXCK7dxs5BvMtT2TLYJxa3bOGZVP6FZL8GOZhGMHmGCyqeyIoKBnT5jC867Lpf/gYMBhnaUMCzviVLm5QUjGXRYGMHUSycH6L89USMJWnhscF8F+tFwzci+Fg4x+JB8A5hjKWw/tPPACZ044sz42Hyl8LT0cnawvlyqOJUQqyK3SmkP2zl9Y4IEKGVk1PcVr557poY5y0K/sP64QKBgQDtPh74uoFLdFe7nXFlgCVgoOUNkDzHp3v54py1QXNSrcPCn6cu3IIB7WoCgivXRxn54CtJT30qpmcGse7NA0b6imbqP3gPKQpq7skrJ7OVpdbwFpk8ZWfbo6quaJX5BSB5PP4e4IMif8RTLsQN+ZRRKzYMrhWAAiDoss7/jrLkCQKBgQC2rbd8NvJ3bU6/j9yfKky1VVuyNrEtLEWnfwVbTO75FbYowqpHl43iQ5wbAfRzejfIec02aVuv9rzZ/u29/ZAhwYGlhx/JfdhhqdqYl8OZ9KaZBDPL0DyUDpH5ag1pwM0J9d3hwLZLwh+5RHVxCagW8V4aopp2IA9ZKR3NIaH+AwKBgQCJHhxceb7801NZtlex6Gyx+owoWLMoWd7HwHf+MsPqh0/bjtbbuMrTPlrE9GNOW2PiDfQ2vOCE5AQr1G9GwpwldhShUumbKYdB4p0Wei3LUjx2KzmJtLpgMaoWf5Aeix4HtH7TbcOdDudzhFYAFb3pY+YrUfpPL+vBUoddISa3sQKBgHQ1wILRhZ95tt3gC85pH1E/0sxlRILjJhBump7BK0jjifUWGTLf84+byQPZU4PenPv4mBZkIcKn6O2Y4e66DmWa7vm5IvyQBQXkAr6N1Q3sd8rhxH2Eo/Tf6/bJg2ziDM2oThVd/dnlzT6TkU5ugTLPKzkwdahtrrXelSm4mo+3AoGAKCyPOTuN3Dl3SzL7j8oITBRvQsd42aEcn2Ycymh6bp1JvTo/UXuHNOjuebI0zBYrQDn6uws33xi8cKqWCJOXk4YnzKKnzhz0gAZRLt0wdLpx0fp042wKwcEeIZpHaPDzc1l8rZc8aFLif89H8EWIZF64EyVvXxW9lrBmZUD44bk=";
    private final String CHARSET = "UTF-8";
    // 支付宝公钥
    private final String ALIPAY_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAqUsnCkfUkMBi2jmiDmqecVfPLYB00q4qLDYT8Gijom7lwQXjFQYjb6ZzZrRQcB9Tceu7HmTounqZKmhz2t3xrBGxmRx0HgxLbAlWXdef631dC5WUu8VLwEwR+t04YHs6wvllN4uFmVKs8AeUZ/J2JBKO1Bcouv9a5M+kUcLy6sVVMkxm4nruNIiDJXMLzyw9EwtAeXxEXQq4vTFNeJRJ1hw++Qx8EPymmAVwxXuDBa/wcHZJNi4eACSE625g37KTZszqVaE4g6ijactk9fJMSXZpT6oW7tPQyKbNZitcEcaQ/IRwB4YCZzd4rgaBYY3V2wtwm66dUKtzHmqvvQKaGwIDAQAB";
    //这是沙箱接口路径
    private final String GATEWAY_URL ="https://openapi-sandbox.dl.alipaydev.com/gateway.do";
    private final String FORMAT = "JSON";
    //签名方式
    private final String SIGN_TYPE = "RSA2";
    //支付宝异步通知路径,付款完毕后会异步调用本项目的方法,必须为公网地址
    private final String NOTIFY_URL = "http://47.95.178.56:443/aliyun/pay/yibu";
    //支付宝同步通知路径,也就是当付款完毕后跳转本项目的页面,可以不是公网地址
    private final String RETURN_URL = "http://IP/entertainment/payment";




    @ApiOperation(value = "支付宝支付")
    @RequestMapping(value = "/payZFB", method = RequestMethod.POST)
    @ResponseBody
    public Result payZFB(@RequestBody PaymentParam paymentParam) throws AlipayApiException {
        AlipayClient alipayClient = new DefaultAlipayClient(GATEWAY_URL, APP_ID, APP_PRIVATE_KEY, FORMAT, CHARSET, ALIPAY_PUBLIC_KEY, SIGN_TYPE);
        AlipayTradePagePayRequest request = new AlipayTradePagePayRequest();
        //异步接收地址，仅支持http/https，公网可访问
        request.setNotifyUrl(NOTIFY_URL);
        //同步跳转地址，仅支持http/https
        request.setReturnUrl(paymentParam.getReturnUrl());
        JSONObject json = new JSONObject();
        json.put("outTradeNo", paymentParam.getOutTradeNo());
        String params = json.toString();
        /******必传参数******/
        JSONObject bizContent = new JSONObject();
        //商户订单号，商家自定义，保持唯一性
        bizContent.put("out_trade_no", paymentParam.getOutTradeNo());
        //支付金额，最小值0.01元
        bizContent.put("total_amount", (paymentParam.getTotalAmount()).toString());
        //订单标题，不可使用特殊符号
        bizContent.put("subject", paymentParam.getSubject());
        //电脑网站支付场景固定传值FAST_INSTANT_TRADE_PAY
        bizContent.put("product_code", paymentParam.getProductCode());
        //支付宝请求的传值 可以在req中获取
        bizContent.put("body", params);
        /******可选参数******/
        request.setBizContent(bizContent.toString());
        AlipayTradePagePayResponse response = alipayClient.pageExecute(request);
        return Result.success(response.getBody());
    }



}
