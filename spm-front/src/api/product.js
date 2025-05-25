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

//更新状态
export function productStatusUpdate(pid, status) {
    return myAxios({
        url: '/api/product/status',
        method: 'put',
        data: {
            id: pid,
            status: status
        }
    })
}

export function productCommentCount(pid) {
    return myAxios({
        url: `/api/product/${pid}/reviews/count`,
        method: 'get',
        params: {
            pid: pid
        }
    })
}

//获取评论列表
export function productCommentGet(pid, page = 1,limit = 3) {
    return myAxios({
        url: `/api/product/${pid}/reviews`,
        method: 'get',
        params: {
            page: page,
            limit: limit
        }
    })
}
/* 示例   {
            "id": 1,
            "userId": 5,
            "productId": 1,
            "content": "good,sddfssdfddfdgosdfdsosdfd!",
            "rating": 5,
            "createdAt": "2025-03-21 10:35:23",
            "status": 1
        }, */



//发表评论
export function productCommentPost(uid, pid, rating, content,) {
    return myAxios({
        url: `/api/product/${pid}/reviews`,
        method: 'post',
        data: {
            userId: uid,
            rating: rating,
            content: content
        }
    })
}


//删除评论
export function productCommentDelete(cid,pid = 1) {
    return myAxios({
        url: `/api/product/${pid}/reviews/${cid}`,
        method: 'delete',
        params: {
        }
    })
}

//获取商品销量
export function productSales(pid, status = -1) {
    return myAxios({
        url: `/api/product/${pid}/sales`,
        method: 'get',
        params: {
            status: status
        }
    })
}

//获取销量详情
export function productSalesDetail(pid, status = -1, start, end) {
    return myAxios({
        url: `/api/product/${pid}/sales/detail`,
        method: 'get',
        params: {
            status: status,
            startTime: start,
            endTime: end
        }
    })
}

//随机选取指定数量的商品
export function productRandom(limit = 4) {
    return myAxios({
        url: `/api/product/random`,
        method: 'get',
        params: {
            limit: limit
        }
    })
}