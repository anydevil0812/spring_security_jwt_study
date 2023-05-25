package com.example.token.config;

import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.example.token.filter.JwtFilter;
import com.example.token.filter.TokenProvider;

public class JwtSecurityConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {
    private TokenProvider tokenProvider;
    public JwtSecurityConfig(TokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }
    
    // Security에 필터 등록
    @Override
    public void configure(HttpSecurity http) {
        http.addFilterBefore(new JwtFilter(tokenProvider),
            UsernamePasswordAuthenticationFilter.class); 
    }
}
