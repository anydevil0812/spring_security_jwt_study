package com.example.jwt.config;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;


@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
		
	@Override
	protected void configure(HttpSecurity http) throws Exception{
		http.csrf().disable(); // CSRF 미사용
		
		http.authorizeRequests() // 인증 절차 설정
				.antMatchers("/", "/home").permitAll() // 인증을 거치지 않는 URL 설정
				.anyRequest().authenticated() // 인증을 거쳐야 하는 URL 설정(예외로 지정한 URL 제외 모두)
				.and()
            .formLogin() // 로그인 or 로그아웃 세팅
                .loginPage("/login") // 로그인 페이지 설정
                .defaultSuccessUrl("/") // 인증 성공했을 경우 이동하는 페이지 설정
                .permitAll() // 인증절차 없이 접근 허용
                .and()
            .logout()
                .permitAll(); 
	}
	
	@Bean
	@Override
	public UserDetailsService userDetailsService() {
		// 인메모리 테스트 DB
		UserDetails user =
			 User.withDefaultPasswordEncoder()
				.username("user")
				.password("password")
				.roles("USER")
				.build();

		return new InMemoryUserDetailsManager(user);
	}
	
}
