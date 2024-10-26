package com.krasnopolskyi.fitcoach.config;

import com.krasnopolskyi.fitcoach.http.interceptor.ControllerLogInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {
    private final ControllerLogInterceptor controllerLogInterceptor;

    // add custom interceptor for spring configuration
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(controllerLogInterceptor)
                .addPathPatterns("/**"); // Apply to all endpoints
    }
}
