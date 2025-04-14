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

//按订单号查询
export function orderCheckout(oid) {
    return myAxios({
        url: '/api/order/orderdetail',
        method: 'get',
        params: {
            orderId: oid
        }
    })
}

//获取orderitem组
export function orderItem(oid,pid) {
    return myAxios({
        url: '/api/order/getItem',
        method: 'get',
        params: {
            orderId: oid,
            productId: pid
        }
    })
}

//商家订单获取
export function orderSeller(status = 0) {
    return myAxios({
        url: '/api/order/seller',
        method: 'get',
        params: {
            status: status
        }
    })
}

//订单状态更新
export function orderUpdate(orderId,productId) {
    return myAxios({
        url: '/api/order/update',
        method: 'get',
        params: {
            orderId: orderId,
            productId: productId
        }
    })
}
//订单发货
export function orderSend(orderId,trackingNumber) {
    return myAxios({
        url: '/api/order/send',
        method: 'put',
        params: {
            orderId: orderId,
            trackingNumber: trackingNumber
        }
    })
}

//全局大订单状态更新
export function orderStateUpdate(orderId,status) {
    return myAxios({
        url: `/api/order/status/${orderId}/${status}`,
        method: 'put',
    })
}