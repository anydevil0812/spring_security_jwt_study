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
	private String username; // ���� id
    private String password; // ���� ��й�ȣ
    private String name; // ���� ����� �̸�
    private boolean accountNonExpired; // ����� ��������
    private boolean accountNonLocked; // ����ִ� ��������
    private boolean credentialsNonExpired; // ������ ��й�ȣ�� ����Ǿ�����
    private boolean enabled; // ��밡���� ��������
    private Collection<? extends GrantedAuthority> authorities; // ������ ������ �ִ� ���� ���

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
