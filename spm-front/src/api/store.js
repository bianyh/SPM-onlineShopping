//store.js
import myAxios from "@/config/axios";

//测
export function storeInfo(sid) {
    return myAxios({
        url: `/api/seller/get/${sid}`,
        method: 'get',
        params: {}
    })
}