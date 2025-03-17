//temp.js

import myAxios from "@/config/axios";

//测
export function tempTest(query) {
    return myAxios({
        url: '/api/',
        method: 'get',
        params: query
    })
}