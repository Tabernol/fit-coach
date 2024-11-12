package com.krasnopolskyi.fitcoach.config.security;

import com.krasnopolskyi.fitcoach.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public static final List<String> FREE_PATHS = Arrays.asList(
            "/api/v1/authn/login/**",
            "/api/v1/trainees/create",
            "/api/v1/trainers/create"
//            "/api/v1/training-types" // Allow this end-point for creating Front-end part
    );

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config)
            throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(request -> request.requestMatchers(
                                "/swagger-ui/**",        // Exclude Swagger UI path
                                "/v3/api-docs/**",       // Exclude OpenAPI specification
                                "/swagger-resources/**", // Exclude Swagger resources
//                                "/webjars/**",           // Exclude webjars for Swagger UI dependencies
                                FREE_PATHS.get(0),
                                FREE_PATHS.get(1),
                                FREE_PATHS.get(2)
//                                FREE_PATHS.get(3) //training-types
                                ).permitAll()
                        .anyRequest().authenticated())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // Make it stateless
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
//                .httpBasic(withDefaults());
        return http.build();
    }
}
