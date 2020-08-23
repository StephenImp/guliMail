package com.atguigu.gulimall.auth.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @Description TODO
 * @Author 鲁班爱喝旺仔
 * @Date 2020/4/20 15:05
 * @Version 1.0
 **/
@Configuration
public class GulimallWebConfig implements WebMvcConfigurer {

    /**
     * 视图映射
     * @param registry
     */
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {

//        registry.addViewController("/login.html").setViewName("login");


        // url 为 /reg.html  则 跳转 reg  就是去 reg.html
        registry.addViewController("/reg.html").setViewName("reg");
    }
}
