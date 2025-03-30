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
export function storeUpdate(username,name,address) {
    return myAxios({
        url: `/api/seller/update`,
        method: 'put',
        data: {
            name: "string",
            description: "string",
            backpicture: "string",
            status: 0,
            id: "string"
        }
    })
}
