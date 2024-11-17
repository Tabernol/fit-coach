package com.krasnopolskyi.fitcoach.config.security;

import com.krasnopolskyi.fitcoach.config.security.filter.CheckUsernameFilter;
import com.krasnopolskyi.fitcoach.config.security.filter.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.util.AntPathMatcher;

import java.util.Arrays;
import java.util.List;


@Configuration
@EnableWebSecurity()
@EnableMethodSecurity()
@RequiredArgsConstructor()
public class SecurityConfig {
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final CheckUsernameFilter checkUsernameFilter;

    private static final List<String> FREE_PATHS = Arrays.asList(
            "/swagger-ui/**",
            "/v3/api-docs/**",
            "/swagger-resources/**",
            "/api/v1/authn/login",
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
                .authorizeHttpRequests(request -> request
                        .requestMatchers(FREE_PATHS.toArray(new String[0]))
                        .permitAll()  // Permit all for specified paths
                        .anyRequest().authenticated())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterAfter(checkUsernameFilter, JwtAuthenticationFilter.class); // Add CheckUsernameFilter after JWT filter
        return http.build();
    }

    // Utility method to check if the request path should bypass
    public static boolean isExcludedPath(String requestPath) {
        AntPathMatcher pathMatcher = new AntPathMatcher();
        return FREE_PATHS.stream().anyMatch(path -> pathMatcher.match(path, requestPath));
    }
}
