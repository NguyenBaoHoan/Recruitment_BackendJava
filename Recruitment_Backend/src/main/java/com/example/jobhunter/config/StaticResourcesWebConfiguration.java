package com.example.jobhunter.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class StaticResourcesWebConfiguration implements WebMvcConfigurer {

    @Value("${hoan.upload-file.base-uri}")
    private String baseURI;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Đảm bảo property kết thúc bằng /
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations(baseURI); // ví dụ: file:C:/Users/ADMIN/uploads/
    }
}
