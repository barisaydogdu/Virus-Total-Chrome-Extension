package com.baris.VirusTotalAPI.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import java.util.Collections;

@Configuration
@EnableWebSecurity
public class SecurityConfig {


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())  // CSRF korumasını devre dışı bırakır
                .authorizeHttpRequests(authorize -> authorize
                        .anyRequest().permitAll() // Diğer tüm istekler kimlik doğrulaması gerektirir
                );
           //     .csrf().disable(); // CSRF korumasını devre dışı bırak
    //.addFilter(new BasicAuthenticationFilter(authenticationManager()));
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        return new ProviderManager(Collections.singletonList(new DaoAuthenticationProvider()));
    }
}