//cart.js

import myAxios from "@/config/axios";

//添加商品到购物车
export function cartAppend(pid, quantity, spec = '') {
    return myAxios({
        url: '/api/cart',
        method: 'post',
        data: {
            'productId': pid,
            'quantity': quantity,
            'spec': spec
        }
    })
}

//展示用户购物车
export function cartShow() {
    return myAxios({
        url: '/api/cart',
        method: 'get',
        params: []
    })
}

//移除用户购物车商品
export function cartRemove(pid) {
    console.log(pid)
    return myAxios({
        url: `/api/cart/delete/${pid}`,
        method: 'delete',
    })
}