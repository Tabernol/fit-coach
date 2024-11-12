package com.krasnopolskyi.fitcoach.config;

import com.krasnopolskyi.fitcoach.http.interceptor.ControllerLogInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {
    private final ControllerLogInterceptor controllerLogInterceptor;

    // add custom interceptors for spring configuration
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // Register the logging interceptor for all paths
        registry.addInterceptor(controllerLogInterceptor)
                .addPathPatterns("/**"); // Apply to all endpoints
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // Allow CORS on all paths
                .allowedOrigins("http://localhost:3000") // can be original front-end site
                .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE") // Allowed methods
                .allowedHeaders("Authorization", "Content-Type") // Allowed headers
                .allowCredentials(true) // Allow cookies to be sent
                .maxAge(3600); // Cache CORS pre-flight response for 1 hour
    }
}
