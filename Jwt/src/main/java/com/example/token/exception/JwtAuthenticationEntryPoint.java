package com.example.token.exception;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
   @Override
   public void commence(HttpServletRequest request,
                        HttpServletResponse response,
                        AuthenticationException authException) throws IOException {
      // ��ȿ�� �ڰ������� �������� �ʰ� �����Ϸ� �Ҷ� 401
      response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
   }
}