package com.example.token.config;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.CorsFilter;

import com.example.token.exception.JwtAccessDeniedHandler;
import com.example.token.exception.JwtAuthenticationEntryPoint;
import com.example.token.filter.TokenProvider;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter{
	
	 private final TokenProvider tokenProvider;
     private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
     private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

	 public SecurityConfig(
			TokenProvider tokenProvider,
	        JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint,
	        JwtAccessDeniedHandler jwtAccessDeniedHandler) 
	 	{
	        this.tokenProvider = tokenProvider;
	        this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
	        this.jwtAccessDeniedHandler = jwtAccessDeniedHandler;
	    }
	
	@Bean
	public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    } 
	 
	@Override
	public void configure(WebSecurity web) {
		web.ignoring()
		   .antMatchers("/h2-console/**", "/favicon.ico");
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable(); // JWT�� ����ϱ� ���� CSRF �̻��
        
        http.exceptionHandling() // ���� ������ exception�� �߰�
        	.authenticationEntryPoint(jwtAuthenticationEntryPoint)
            .accessDeniedHandler(jwtAccessDeniedHandler)
            
            .and() // h2-console ������ ���� ����
            .headers()
            .frameOptions()
            .sameOrigin()
            
 			.and()
            .sessionManagement() // ���� ���� ����
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // ������ ������� �ʱ� ������ STATELESS�� ����
            
            .and()
            .authorizeHttpRequests()  // ���� ���� ����
            .antMatchers("/api/hello", "/api/authenticate", "/api/signup").permitAll() // ���� ���� ���� ������ �ּ� ����
            .anyRequest().authenticated() // ������ �̿��� �ּҴ� ���� �䱸
            
            .and()
            .apply(new JwtSecurityConfig(tokenProvider)); // spring security ������ JwtFilter ���
	}
	
}
