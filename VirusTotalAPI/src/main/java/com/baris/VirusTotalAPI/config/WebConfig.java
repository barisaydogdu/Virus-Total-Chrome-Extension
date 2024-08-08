package com.baris.VirusTotalAPI.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("chrome-extension://fnnjhllnocdmmlbiobpdaidfpdgkajan")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "HEAD")
                .allowCredentials(true);

        registry.addMapping("/scan")
                .allowedOrigins("http://localhost:8080")
                .allowedMethods("POST");
    }

}