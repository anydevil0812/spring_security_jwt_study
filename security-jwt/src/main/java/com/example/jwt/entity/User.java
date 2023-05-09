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
	private String username; // ���� id
    private String password; // ���� ��й�ȣ
    private String name; // ���� ����� �̸�
    private boolean isAccountNonExpired; // ����� ��������
    private boolean isAccountNonLocked; // ����ִ� ��������
    private boolean isCredentialsNonExpired; // ������ ��й�ȣ�� ����Ǿ�����
    private boolean isEnabled; // ��밡���� ��������
    private Collection<? extends GrantedAuthority> authorities; // ������ ������ �ִ� ���� ���

	   
}
