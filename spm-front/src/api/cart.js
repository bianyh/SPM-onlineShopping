//cart.js

import myAxios from "@/config/axios";

//添加商品到购物车
export function cartAppend(pid,quantity, spec = '') {
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