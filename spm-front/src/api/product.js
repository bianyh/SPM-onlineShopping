//product.js

import myAxios from "@/config/axios";

//搜索商品
export function searchProduct(keyword, page = 1, limit = 20) {
    return myAxios({
        url: '/api/product',
        method: 'get',
        params: {
            'keyword': keyword,
            'page': page,
            'limit': limit
        }
    })
}

//精确查找
export function productInfo(id) {
    return myAxios({
        url: '/api/product/search/' + id,
        method: 'get',
        params: {
        }
    })
}


// 更新商品信息
export function productUpdate(name, description, pictures, stock, price, storeId, status = 0, pid = -1,) {
    return myAxios({
        url: `/api/seller/${pid}/update`,
        method: 'put',
        data: {
            name: name,
            description: description,
            pictures: pictures,
            stock: stock,
            price: price,
            status: status,
            storeId: storeId,
        }
    })
}