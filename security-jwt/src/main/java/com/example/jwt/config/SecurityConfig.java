package com.example.jwt.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.example.jwt.service.UserServiceImpl;

@Configuration // 환경 변수 설정에 대한 클래스 임을 등록
@EnableWebSecurity //  Spring Security에 대한 설정을 할 클래스라 지정
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	UserServiceImpl userService;
	
	@Override
	protected void configure(HttpSecurity http) throws Exception{
//		http.csrf().disable(); // CSRF 미사용
		
		http.authorizeRequests() // 인증 절차 설정
		    .antMatchers("/admin").hasRole("ADMIN") // /admin으로 시작하는 경로는 ADMIN 권한을 가진 사용자만 접근 가능
			.antMatchers("/user/myinfo").hasAnyRole("USER", "ADMIN") // /user/myinfo 경로는 USER 권한을 가진 사용자만 접근 가능
			.anyRequest().permitAll() // 앞선 경로 이외의 모든 경로 인증 없이 접근 가능
		.and() // 로그인 설정
	        .formLogin()
			.loginPage("/user/login") // 로그인 페이지 설정
			.defaultSuccessUrl("/user/login/result") // 로그인 성공 시 페이지 설정
			.permitAll()
		.and() // 로그아웃 설정
			.logout()
			.logoutRequestMatcher(new AntPathRequestMatcher("/user/logout")) // 로그아웃 페이지 설정
			.logoutSuccessUrl("/user/logout/result") // 로그아웃 성공 시 페이지 설정
			.invalidateHttpSession(true) // Http 세션 초기화
		.and()
			.exceptionHandling().accessDeniedPage("/user/denied"); // 403 예외처리 핸들링

	}
	
	@Override
	public void configure(AuthenticationManagerBuilder auth) throws Exception {
	      auth.userDetailsService(userService).passwordEncoder(new BCryptPasswordEncoder());
	}
	
	
}