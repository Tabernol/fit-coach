package com.krasnopolskyi.fitcoach.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
public class SecurityConfiguration {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
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


//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//
//        http
//                .authorizeHttpRequests((authz) -> {
//                            try {
//                                authz
//                                        .anyRequest().authenticated()
//                                        .and().formLogin()
//                                        .loginPage("/api/v1/authn/login/**")
//                                        .defaultSuccessUrl("/",true)
//                                        .permitAll()
//                                        .and()
//                                        .logout()
//                                        .invalidateHttpSession(true)
//                                        .clearAuthentication(true)
//                                        .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
//                                        .logoutSuccessUrl("/login?logout")
//                                        .permitAll()
//                                        .and()
//                                        .exceptionHandling()
//                                        .accessDeniedHandler(accessDeniedHandler)
//                                ;
//                            } catch (Exception e) {
//                                throw new RuntimeException(e);
//                            }
//                        }
//                )
//
//                .authenticationManager(new CustomAuthenticationManager(shouldAuth));
//
//        return http.build();
//    }


//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http.csrf(AbstractHttpConfigurer::disable)
//                .authorizeHttpRequests(request -> request.requestMatchers(
//                                "/user/add",
//                                "/user/authenticate",
//                                "/swagger-ui/**",
//                                "/v3/api-docs/**",
//                                "/swagger-resources/**",
//                                "/swagger-ui.html"
//                        )
//                        .permitAll()
//                        .anyRequest().authenticated())
//                .sessionManagement(manager -> manager.sessionCreationPolicy(STATELESS))
//                .authenticationProvider(authenticationProvider())
//                .addFilterBefore(
//                        jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
//        return http.build();
//    }
}
