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
		http.csrf().disable(); // CSRF �̻��
		
		http.authorizeRequests() // ���� ���� ����
				.antMatchers("/", "/home").permitAll() // ������ ��ġ�� �ʴ� URL ����
				.anyRequest().authenticated() // ������ ���ľ� �ϴ� URL ����(���ܷ� ������ URL ���� ���)
				.and()
            .formLogin() // �α��� or �α׾ƿ� ����
                .loginPage("/login") // �α��� ������ ����
                .defaultSuccessUrl("/") // ���� �������� ��� �̵��ϴ� ������ ����
                .permitAll() // �������� ���� ���� ���
                .and()
            .logout()
                .permitAll(); 
	}
	
	@Bean
	@Override
	public UserDetailsService userDetailsService() {
		// �θ޸� �׽�Ʈ DB
		UserDetails user =
			 User.withDefaultPasswordEncoder()
				.username("user")
				.password("password")
				.roles("USER")
				.build();

		return new InMemoryUserDetailsManager(user);
	}
	
}
