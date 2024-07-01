package com.nonameplz.dailyhandler.config;

import com.nonameplz.dailyhandler.interceptors.authenticationInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.nonameplz.dailyhandler.interceptors.loginInterceptor;

@Configuration
public class webConfig implements WebMvcConfigurer {

    @Autowired
    private loginInterceptor loginInterceptor;
    @Autowired
    private authenticationInterceptor authenticationInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authenticationInterceptor)
                .addPathPatterns(
                        "/authentication/login",
                        "/authentication/register");

        registry.addInterceptor(loginInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns(
                        "/authentication/login",
                        "/authentication/register");
    }

}
