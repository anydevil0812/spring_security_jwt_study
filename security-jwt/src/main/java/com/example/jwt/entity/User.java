package com.example.jwt.entity;

import java.util.Collection;

import javax.persistence.Entity;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.Data;

@Data
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
	   
}
