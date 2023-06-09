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
	   
	   // usernamePasswordAuthentication 필터
	   // 토큰을 받아옴 => 토큰으로 Authencation 객체를 얻음 => SecurityContext에 Authentication 객체에 저장
	   @Override
	   public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
	      HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
	      String jwt = resolveToken(httpServletRequest); // 토큰을 얻어옴
	      String requestURI = httpServletRequest.getRequestURI();

	      if (StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt)) {
	         Authentication authentication = tokenProvider.getAuthentication(jwt); // 토큰으로 Authentication 객체 얻어옴
	         
	         // SecurityContextHolder를 통해 SecurityContext에 Authentication 객체 저장
	         SecurityContextHolder.getContext().setAuthentication(authentication);  
	         
	         logger.debug("Security Context에 '{}' 인증 정보를 저장했습니다, uri: {}", authentication.getName(), requestURI);
	      } else {
	         logger.debug("유효한 JWT 토큰이 없습니다, uri: {}", requestURI);
	      }
	      filterChain.doFilter(servletRequest, servletResponse); // FilterChain으로 여러개의 필터가 있으므로 재귀 적용
	   }
	   
	   // 토큰 반환
	   private String resolveToken(HttpServletRequest request) {
	      String bearerToken = request.getHeader("Authorization");

	      if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
	         return bearerToken.substring(7);
	      }

	      return null;
	   }
}
