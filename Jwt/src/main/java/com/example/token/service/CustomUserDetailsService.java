package com.example.token.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.example.token.entity.User;
import com.example.token.repository.UserRepository;

@Component("userDetailsService")
public class CustomUserDetailsService implements UserDetailsService {
	
   private final UserRepository userRepository;

   public CustomUserDetailsService(UserRepository userRepository) {
      this.userRepository = userRepository;
   }

   @Override
   @Transactional
   public UserDetails loadUserByUsername(final String username) {
      return userRepository.findOneWithAuthoritiesByUsername(username)
         .map(user -> createUser(username, user))
         .orElseThrow(() -> new UsernameNotFoundException(username + " -> �����ͺ��̽����� ã�� �� �����ϴ�."));
   }

   private org.springframework.security.core.userdetails.User createUser(String username, User user) {
      if (!user.isActivated()) {
         throw new RuntimeException(username + " -> Ȱ��ȭ�Ǿ� ���� �ʽ��ϴ�.");
      }

      List<GrantedAuthority> grantedAuthorities = user.getAuthorities().stream()
              .map(authority -> new SimpleGrantedAuthority(authority.getAuthorityName()))
              .collect(Collectors.toList());

      return new org.springframework.security.core.userdetails.User(user.getUsername(),
              user.getPassword(),
              grantedAuthorities);
   }
}