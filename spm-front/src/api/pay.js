//temp.js

import myAxios from "@/config/axios";

//支付宝支付
export function payZFB(params) {
    return myAxios({
        url: '/api/aliyun/pay/payZFB',
        method: 'post',
        data: params
    })
}