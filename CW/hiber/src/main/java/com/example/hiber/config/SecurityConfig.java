package com.example.hiber.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable())            // отключаем CSRF
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll()        // разрешаем доступ ко всем страницам
                )
                .formLogin(login -> login.disable())      // отключаем страницу логина
                .httpBasic(basic -> basic.disable());     // отключаем basic-auth

        return http.build();
    }
}
