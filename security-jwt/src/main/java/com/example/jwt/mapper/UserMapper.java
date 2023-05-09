package com.example.jwt.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.example.jwt.entity.User;

@Mapper
public interface UserMapper {
	public List<User> Userlist();
	public User readUser(String username);
	public List<String> readAuthority(String username);
	public void createUser(User user);
	public void createAuthority(User user);
	public void deleteUser(String username);
	public void deleteAuthority(String username);
}
