//order.js

import myAxios from "@/config/axios";

//提交订单
export function orderSubmit(orderdata) {
    return myAxios({
        url: '/api/order',
        method: 'post',
        data: orderdata
    })
}

//展示订单
export function orderShow() {
    return myAxios({
        url: '/api/order/list',
        method: 'get',
        params: []
    })
}

//详情
export function orderDetail(oid) {
    return myAxios({
        url: '/api/order/detail',
        method: 'get',
        params: {
            orderId: oid
        }
    })
}
