package com.example.jwt.entity;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class User implements UserDetails {
	   
	private static final long serialVersionUID = -5034392015105278124L;
	private String username; // 계정 id
    private String password; // 계정 비밀번호
    private String name; // 계정 사용자 이름
    private boolean accountNonExpired; // 만료된 계정인지
    private boolean accountNonLocked; // 잠겨있는 계정인지
    private boolean credentialsNonExpired; // 계정의 비밀번호가 만료되었는지
    private boolean enabled; // 사용가능한 계정인지
    private Collection<? extends GrantedAuthority> authorities; // 계정이 가지고 있는 권한 목록

	public User(String username, String password, String name, boolean accountNonExpired, boolean accountNonLocked,
			boolean credentialsNonExpired, boolean enabled) {
		super();
		this.username = username;
		this.password = password;
		this.name = name;
		this.accountNonExpired = accountNonExpired;
		this.accountNonLocked = accountNonLocked;
		this.credentialsNonExpired = credentialsNonExpired;
		this.enabled = enabled;
	}
}
