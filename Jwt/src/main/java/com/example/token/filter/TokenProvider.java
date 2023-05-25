package com.example.token.filter;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Component
public class TokenProvider implements InitializingBean {

   private final Logger logger = LoggerFactory.getLogger(TokenProvider.class);
   private static final String AUTHORITIES_KEY = "auth";
   private final String secret;
   private final long tokenValidityInMilliseconds;
   private Key key;

   public TokenProvider(@Value("${jwt.secret}") String secret, @Value("${jwt.token-valid-time}") long tokenValidTime) {
      this.secret = secret;
      this.tokenValidityInMilliseconds = tokenValidTime * 1000;
   }

   @Override
   public void afterPropertiesSet() {
      byte[] keyBytes = Decoders.BASE64.decode(secret);
      this.key = Keys.hmacShaKeyFor(keyBytes); // Keys.hmacShaKeyFor() = ȯ�漳���� �ִ� ���Ű ���ڿ��� byte[]�� �����ϸ� SecretKey �ν��Ͻ� ����
   }
   
   // ��ū ����
   public String createToken(Authentication authentication) {
      String authorities = authentication.getAuthorities().stream()
         .map(GrantedAuthority::getAuthority)
         .collect(Collectors.joining(","));

      long now = (new Date()).getTime();
      Date validity = new Date(now + this.tokenValidityInMilliseconds);

      return Jwts.builder()
         .setSubject(authentication.getName()) // ��ū ���� ����
         .claim(AUTHORITIES_KEY, authorities) // payload�� ���� claim(key:value ������) ����
         .signWith(key, SignatureAlgorithm.HS512) // ����� ��ȣȭ �˰���(HS256) �� signature�� �� secret key ����
         .setExpiration(validity) // ��ȿ�Ⱓ ����
         .compact(); 
   }
   
   // builder = jwt ����(���ڵ�), parserBuilder = jwt ����(���ڵ�)
   public Authentication getAuthentication(String token) {
      Claims claims = Jwts
              .parserBuilder() // JwtParseBuilder �ν��Ͻ� ����
              .setSigningKey(key) // ������ ���� key ����
              .build() // jwtParser ��ȯ
              .parseClaimsJws(token) // ��ū�� Jws�� �Ľ�
              .getBody(); // ��ū�� �����ߴ� data���� ��� Claims ��ȯ 

      Collection<? extends GrantedAuthority> authorities =
         Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
            .map(SimpleGrantedAuthority::new)
            .collect(Collectors.toList());

      User principal = new User(claims.getSubject(), "", authorities);
      
      //  UsernamePasswordAuthenticationToken = Authentication �������̽��� ����ü (Authentication ��ü�� ����)
      return new UsernamePasswordAuthenticationToken(principal, token, authorities); // token�� credentials(Password�� ��)
   }
   
   // ��ū ����
   public boolean validateToken(String token) {
      try {
         Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
         return true;
      } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
         logger.info("�߸��� JWT �����Դϴ�.");
      } catch (ExpiredJwtException e) {
         logger.info("����� JWT ��ū�Դϴ�.");
      } catch (UnsupportedJwtException e) {
         logger.info("�������� �ʴ� JWT ��ū�Դϴ�.");
      } catch (IllegalArgumentException e) {
         logger.info("JWT ��ū�� �߸��Ǿ����ϴ�.");
      }
      return false;
   }
}
