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

@Configuration // ȯ�� ���� ������ ���� Ŭ���� ���� ���
@EnableWebSecurity //  Spring Security�� ���� ������ �� Ŭ������ ����
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	UserServiceImpl userService;
	
	@Override
	protected void configure(HttpSecurity http) throws Exception{
//		http.csrf().disable(); // CSRF �̻��
		
		http.authorizeRequests() // ���� ���� ����
		    .antMatchers("/admin").hasRole("ADMIN") // /admin���� �����ϴ� ��δ� ADMIN ������ ���� ����ڸ� ���� ����
			.antMatchers("/user/myinfo").hasAnyRole("USER", "ADMIN") // /user/myinfo ��δ� USER ������ ���� ����ڸ� ���� ����
			.anyRequest().permitAll() // �ռ� ��� �̿��� ��� ��� ���� ���� ���� ����
		.and() // �α��� ����
	        .formLogin()
			.loginPage("/user/login") // �α��� ������ ����
			.defaultSuccessUrl("/user/login/result") // �α��� ���� �� ������ ����
			.permitAll()
		.and() // �α׾ƿ� ����
			.logout()
			.logoutRequestMatcher(new AntPathRequestMatcher("/user/logout")) // �α׾ƿ� ������ ����
			.logoutSuccessUrl("/user/logout/result") // �α׾ƿ� ���� �� ������ ����
			.invalidateHttpSession(true) // Http ���� �ʱ�ȭ
		.and()
			.exceptionHandling().accessDeniedPage("/user/denied"); // 403 ����ó�� �ڵ鸵

	}
	
	@Override
	public void configure(AuthenticationManagerBuilder auth) throws Exception {
	      auth.userDetailsService(userService).passwordEncoder(new BCryptPasswordEncoder());
	}
	
	
}