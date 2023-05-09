package com.example.jwt.entity;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class User implements UserDetails {
	   
	private static final long serialVersionUID = -5034392015105278124L;
	private String username; // 계정 id
    private String password; // 계정 비밀번호
    private String name; // 계정 사용자 이름
    private boolean isAccountNonExpired; // 만료된 계정인지
    private boolean isAccountNonLocked; // 잠겨있는 계정인지
    private boolean isCredentialsNonExpired; // 계정의 비밀번호가 만료되었는지
    private boolean isEnabled; // 사용가능한 계정인지
    private Collection<? extends GrantedAuthority> authorities; // 계정이 가지고 있는 권한 목록

	   
}
