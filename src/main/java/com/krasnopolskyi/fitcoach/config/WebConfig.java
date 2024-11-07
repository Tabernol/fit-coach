package com.krasnopolskyi.fitcoach.config;

import com.krasnopolskyi.fitcoach.http.interceptor.AuthnInterceptor;
import com.krasnopolskyi.fitcoach.http.interceptor.ControllerLogInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {
    private final ControllerLogInterceptor controllerLogInterceptor;
    private final AuthnInterceptor authnInterceptor;

    // add custom interceptors for spring configuration
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // Register the logging interceptor for all paths
        registry.addInterceptor(controllerLogInterceptor)
                .addPathPatterns("/**"); // Apply to all endpoints


        // Register the authentication interceptor for secured paths
        registry.addInterceptor(authnInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns(            // Exclude these paths from security
                        "/swagger-ui/**",        // Exclude Swagger UI path
                        "/v3/api-docs/**",       // Exclude OpenAPI specification
                        "/swagger-resources/**", // Exclude Swagger resources
                        "/webjars/**",           // Exclude webjars for Swagger UI dependencies
                        "/api/v1/authn/login/**",   // Exclude login endpoint
                        "/api/v1/trainees/create",
                        "/api/v1/trainers/create"
                );
    }
}
