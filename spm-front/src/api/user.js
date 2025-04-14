//user.js

import myAxios from "@/config/axios";

//用户登录
export function userLogin(username, password) {
    return myAxios({
        url: '/api/auth/login',
        method: 'post',
        data: {
            'username': username,
            'password': password
        }
    })
}

//展示所有用户信息
export function userInfo() {
    var result = myAxios({
        url: '/api/user',
        method: 'get',
        params: undefined
    }) 
    return result
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

//获取用户所有店铺
export function userStore(username) {
    var result = myAxios({
        url: `/api/seller/all/${username}`,
        method: 'get',
        params: []
    }) 
    return result
}

//获取用户所有地址
export function userAddressShow() {
    var result = myAxios({
        url: `/api/address/getAddress`,
        method: 'get',
        params: []
    }) 
    return result
}
//获取地址详细信息
export function userAddressDetail(aid) {
    var result = myAxios({
        url: `/api/address/${aid}`,
        method: 'get',
        params: []
    }) 
    return result
}

//添加用户地址
export function userAddressAppend(recipient,phone,province,city,district,street,isDefault) {
    var result = myAxios({
        url: `/api/address/add`,
        method: 'post',
        data: {
            //id: 1, 自动生成
            recipient: recipient,
            phone: phone,
            province: province,
            city: city,
            district: district,
            street: street,
            isDefault: isDefault,
            //createdAt: '2023-10-01 10:30:45'
        }
    }) 
    return result
}

//更新用户地址
export function userAddressUpdate(id,recipient,phone,province,city,district,street,isDefault) {
    var result = myAxios({
        url: `/api/address/update`,
        method: 'post',
        data: {
            id: id,
            recipient: recipient,
            phone: phone,
            province: province,
            city: city,
            district: district,
            street: street,
            isDefault: isDefault,
            //createdAt: '2023-10-01 10:30:45'
        }
    }) 
    return result
}

//删除地址
export function userAddressDelete(aid) {
    var result = myAxios({
        url: `/api/address/delete`,
        method: 'post',
        data: {
            id: aid
        }
    }) 
    return result
}

//设为默认地址
export function userAddressDefault(aid) {
    var result = myAxios({
        url: `/api/address/default`,
        method: 'post',
        data: {
            id: aid
        }
    }) 
    return result
}
