package com.example.token.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

public class JwtFilter extends GenericFilterBean {

	   private static final Logger logger = LoggerFactory.getLogger(JwtFilter.class);
	   private TokenProvider tokenProvider;
	   
	   public JwtFilter(TokenProvider tokenProvider) {
	      this.tokenProvider = tokenProvider;
	   }
	   
	   // usernamePasswordAuthentication ����
	   // ��ū�� �޾ƿ� => ��ū���� Authencation ��ü�� ���� => SecurityContext�� Authentication ��ü�� ����
	   @Override
	   public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
	      HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
	      String jwt = resolveToken(httpServletRequest); // ��ū�� ����
	      String requestURI = httpServletRequest.getRequestURI();

	      if (StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt)) {
	         Authentication authentication = tokenProvider.getAuthentication(jwt); // ��ū���� Authentication ��ü ����
	         
	         // SecurityContextHolder�� ���� SecurityContext�� Authentication ��ü ����
	         SecurityContextHolder.getContext().setAuthentication(authentication);  
	         
	         logger.debug("Security Context�� '{}' ���� ������ �����߽��ϴ�, uri: {}", authentication.getName(), requestURI);
	      } else {
	         logger.debug("��ȿ�� JWT ��ū�� �����ϴ�, uri: {}", requestURI);
	      }
	      filterChain.doFilter(servletRequest, servletResponse); // FilterChain���� �������� ���Ͱ� �����Ƿ� ��� ����
	   }
	   
	   // ��ū ��ȯ
	   private String resolveToken(HttpServletRequest request) {
	      String bearerToken = request.getHeader("Authorization");

	      if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
	         return bearerToken.substring(7);
	      }

	      return null;
	   }
}
