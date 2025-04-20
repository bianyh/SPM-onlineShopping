package com.example.spm.config;

import com.example.spm.interceptors.LoginInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private LoginInterceptor loginInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //添加拦截器，同时将登录和注册接口除外
        registry.addInterceptor(loginInterceptor)
            .excludePathPatterns(
                "/auth/register",
                "/auth/login",
                "/image/get/**"
            );
    }
}
