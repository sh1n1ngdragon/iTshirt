package com.shop.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Value("${uploadPath}")
    String uploadPath;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 상품 이미지 설정
        registry.addResourceHandler("/images/**")
                .addResourceLocations(uploadPath);  // 로컬 컴퓨터에서 root 경로를 설정

        // 이벤트 이미지 설정
        registry.addResourceHandler("/event-uploads/**")
                .addResourceLocations("file:///C:/shop/event-uploads/");
    }
}
