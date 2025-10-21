package com.project._FALogin.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // 1. Disable CSRF using the lambda DSL (replaces .csrf().disable())
        http.csrf(csrf -> csrf.disable());

        // 2. Configure authorization rules using the lambda DSL (replaces .authorizeHttpRequests())
        http.authorizeHttpRequests(auth -> auth
                .requestMatchers(
                        "/auth/**",
                        "/v3/api-docs/**",
                        "/swagger-ui/**",
                        "/swagger-ui.html"
                ).permitAll() // Public endpoints
                .anyRequest().authenticated()           // All other requests require authentication
        );

        // 3. Set session management policy to STATELESS (replaces .sessionManagement().disable())
        http.sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder();
    }
}