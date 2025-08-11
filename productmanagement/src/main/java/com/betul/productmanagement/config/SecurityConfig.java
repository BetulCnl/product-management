package com.betul.productmanagement.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class SecurityConfig {

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests()
                .requestMatchers("/h2-console/**").permitAll()  // H2 konsoluna izin ver
                .anyRequest().authenticated()                   // Diğer tüm istekler yetki ister
                .and()
                .csrf(csrf -> csrf.disable())                       // CSRF korumasını devre dışı bırak
                .headers(headers -> headers.frameOptions().disable());  // Frame options'u kapat

        return http.build();
    }
}


