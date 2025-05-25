//store.js
import myAxios from "@/config/axios";

//店铺详细信息
export function storeInfo(sid) {
    return myAxios({
        url: `/api/seller/get/${sid}`,
        method: 'get',
        params: {}
    })
}

//展示某人的所有店铺
export function storeAll(username) {
    return myAxios({
        url: `/api/seller/all/${username}`,
        method: 'get',
        params: {}
    })
}

//店铺创建
export function storeCreate(username,name,address) {
    return myAxios({
        url: `/api/seller/register`,
        method: 'post',
        data: {
            username: username,
            name: name,
            address: address
        }
    })
}
//店铺信息更新
export function storeUpdate(name,description,backpicture,id,status,address) {
    return myAxios({
        url: `/api/seller/update`,
        method: 'put',
        data: {
            name: name,
            description: description,
            picturePath: backpicture,
            status: status,
            id: id,
            address: address,
        }
    })
}
//搜索商品
export function storeProducts(sid, page = 1, limit = 10, status = '') {
    return myAxios({
        url: '/api/seller/products',
        method: 'get',
        params: {
            'id': sid,
            'page': page,
            'limit': limit,
            'status': status
        }
    })
}

//展示店铺总销量（自动提取用户uid）
export function storeSales() {
    return myAxios({
        url: '/api/seller/sales',
        method: 'get',
        params: {
        }
    })
}

//展示指定店铺销量详情
export function storeSalesDetail(sid) {
    return myAxios({
        url: `/api/seller/sales/${sid}`,
        method: 'get',
        params: {
        }
    })
}