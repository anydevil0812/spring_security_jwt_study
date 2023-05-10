package com.example.jwt.service;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.example.jwt.entity.User;

public interface UserService extends UserDetailsService {
    public void joinUser(User user);
    public void deleteUser(String username);
}
