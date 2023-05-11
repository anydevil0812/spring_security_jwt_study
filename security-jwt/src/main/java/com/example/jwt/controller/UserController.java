package com.example.jwt.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.jwt.entity.User;
import com.example.jwt.mapper.UserMapper;
import com.example.jwt.service.UserServiceImpl;

@Controller
public class UserController {
	
	@Autowired
	UserMapper userMapper;

	@Autowired 
	UserServiceImpl userService;

	 // 메인 페이지
    @GetMapping("/")
    public String home() {
        return "home";
    }

    // 회원가입 페이지
    @GetMapping("/user/signup")
    public String dispSignup() {
        return "signup";
    }

    // 회원가입 처리
    @PostMapping("/user/signup")
    public String execSignup(User user) {
    	List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		if(user.getName().contains("admin")) {
			authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN")); // ADMIN, USER가 아닌 ROLE_ADMIN, ROLE_USER로 입력!
		} else {
			authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
		}
		user.setAuthorities(authorities);
		System.out.println(user);
    	userService.joinUser(user);
        return "redirect:/user/login";
    }

    // 로그인 페이지
    @GetMapping("/user/login")
    public String dispLogin() {
        return "login";
    }

    // 로그인 결과 페이지
    @GetMapping("/user/login/result")
    public String dispLoginResult() {
        return "loginSuccess";
    }

    // 로그아웃 결과 페이지
    @GetMapping("/user/logout/result")
    public String dispLogout() {
        return "logout";
    }

    // 접근 거부 페이지
    @GetMapping("/user/denied")
    public String dispDenied() {
        return "denied";
    }

    // 내 정보 페이지
    @GetMapping("/user/myinfo")
    public String dispMyInfo() {
        return "myinfo";
    }

    // 어드민 페이지
    @GetMapping("/admin")
    public String dispAdmin() {
        return "admin";
    }
}
