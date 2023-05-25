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
		http.csrf().disable(); // JWT를 사용하기 위해 CSRF 미사용
        
        http.exceptionHandling() // 따로 정의한 exception들 추가
        	.authenticationEntryPoint(jwtAuthenticationEntryPoint)
            .accessDeniedHandler(jwtAccessDeniedHandler)
            
            .and() // h2-console 접근을 위한 설정
            .headers()
            .frameOptions()
            .sameOrigin()
            
 			.and()
            .sessionManagement() // 세션 관리 설정
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // 세션을 사용하지 않기 때문에 STATELESS로 설정
            
            .and()
            .authorizeHttpRequests()  // 인증 절차 설정
            .antMatchers("/api/hello", "/api/authenticate", "/api/signup").permitAll() // 인증 없이 접근 가능한 주소 설정
            .anyRequest().authenticated() // 설정한 이외의 주소는 인증 요구
            
            .and()
            .apply(new JwtSecurityConfig(tokenProvider)); // spring security 구성에 JwtFilter 등록
	}
	
}
