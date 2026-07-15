package com.ecom.pranaya.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${image.upload.dir}")
    private String uploadDir;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Force the path to end with a trailing slash so Spring knows it's a directory
        String location = uploadDir.endsWith("/") ? uploadDir : uploadDir + "/";

        // FIX: Change "/uploads/**" to "/images/**"
        registry.addResourceHandler("/images/**")
                .addResourceLocations("file:" + location);
    }
}