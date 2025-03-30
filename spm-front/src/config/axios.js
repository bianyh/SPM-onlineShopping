//axios.js

import axios from "axios";
import { transParams } from "@/utils/vineUtils";
import { decodeJwt } from "@/utils/jwtUtils";
import { ElMessage } from "element-plus";

axios.defaults.headers['Content-Type'] = 'application/json;charset=utf-8'
// 创建axios实例
const myAxios = axios.create({
    timeout: 5000,
});

// request拦截
myAxios.interceptors.request.use(
    config => {
        // get delete 请求映射params函数
        if (config.method == 'get' || config.method == 'delete') {
            let url = config.url + '?' + transParams(config.params);
            console.log(url)
            url = url.slice(0, -1);
            config.params = {};
            config.url = url;
        }
        //post put 请求参数处理
        if (config.method == 'post' || config.method == 'put') {
            url: config.url;
            data: typeof config.data === 'object' ? JSON.stringify(config.data) : config.data;
        }
        // 添加token
        const token = window.localStorage.getItem("token");
        if (token) {
            // 检查 Token 是否过期
            const decodedToken = decodeJwt(token);
            const currentTimestamp = Math.floor(Date.now() / 1000);
            //console.log(decodedToken)
            if (decodedToken[1].exp < currentTimestamp) {
                // Token 过期，刷新 Token
                // await refreshToken();
                console.log("过期了:" + (currentTimestamp - decodedToken[1].exp).toString)
            }
        }
        //config.headers.Authorization = `Bearer ${token}`;
        config.headers.Authorization = `${token}`;
        return config;
    },
    error => {
        console.error('请求错误：', error);
        return Promise.reject(error);
    }
);

//response 拦截
myAxios.interceptors.response.use(
    response => {
        const res = response.data;
        //根据返回的数据进行处理
        return res;
    },
    error => {
        console.error('请求错误：', error);
        ElMessage({
            message: error
        })
        return Promise.reject(error);
    }
)



export default myAxios;