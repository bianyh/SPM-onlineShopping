//product.js

import myAxios from "@/config/axios";

//搜索商品
export function searchProduct(keyword, page = 0, limit = 20) {
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

//展示所有用户
export function userShow() {
    return myAxios({
        url: '/api/auth/showall',
        method: 'get',
        params: undefined
    })
}

//用户注册
export function userRegister(username, password, email, phoneNumber) {
    return myAxios({
        url: '/api/auth/register',
        method: 'post',
        headers: {
            "Content-Type": 'application/json'
        },
        data: {
            'username': username,
            'password': password,
            'email': email,
            'phone': phoneNumber
        }
    })
}