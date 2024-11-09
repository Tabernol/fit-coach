package com.krasnopolskyi.fitcoach.config;

import com.krasnopolskyi.fitcoach.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {
    private final UserService userService;

    private final PasswordEncoder passwordEncoder;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(request -> request.requestMatchers(
                                "/swagger-ui/**",        // Exclude Swagger UI path
                                "/v3/api-docs/**",       // Exclude OpenAPI specification
                                "/swagger-resources/**", // Exclude Swagger resources
//                                "/webjars/**",           // Exclude webjars for Swagger UI dependencies
                                "/api/v1/authn/login/**",   // Exclude login endpoint
                                "/api/v1/trainees/create",
                                "/api/v1/trainers/create"
                        ).permitAll()
//                .formLogin(login -> login
//                        .loginPage("/api/v1/authn/login")
//                        .permitAll())
//                .logout(logout -> logout
//                        .permitAll())
                        .anyRequest().authenticated())
                .httpBasic(withDefaults());
        return http.build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userService);
        authProvider.setPasswordEncoder(passwordEncoder);
        return authProvider;
    }
}
