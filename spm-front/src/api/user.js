//user.js

import myAxios from "@/config/axios";

//用户登录
export function userLogin(username, password) {
    return myAxios({
        url: '/api/auth/login',
        method: 'post',
        params: {
            'username': username,
            'password': password
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
        method: 'get',
        params: {
            'username': username,
            'password': password,
            'email': email,
            'phone': phoneNumber
        }
    })
}