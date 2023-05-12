package com.example.jwt.filter;
import java.util.Base64;
import java.util.Date;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

@Component
public class JwtTokenProvider {
	private static final Logger logger = LoggerFactory.getLogger(JwtTokenProvider.class);
	
	// 토큰 비밀키
	private String secretKey = "abcdelee";
	
	// 토큰 유효시간
	private long tokenValidTime = 30 * 60 * 1000L;
	
	// secretKey를 Base64로 인코딩 (객체 초기화)
    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }
	
	// 토큰 생성
	public String createToken(Authentication authentication) {
		JwtBuilder builder = Jwts.builder(); // 토큰 생성 builder 객체
		
		final String token = builder.setHeaderParam("typ", "JWT")// 토큰의 타입으로 고정 값
								    .setSubject("login-token")// 토큰 제목 설정
								    .setIssuedAt(new Date()) // 토큰 발행 시간 설정
								    .setExpiration(new Date(System.currentTimeMillis() + tokenValidTime)) // 유효기간
								    .claim("data", authentication) // payload에 담을 claim(key:value 데이터) 설정
								    .signWith(SignatureAlgorithm.HS256, secretKey) // 사용할 암호화 알고리즘(HS256) 및 signature에 들어갈 secret key 설정
								    .compact(); // 직렬화(모든 설정들 하나로 합침)
		return token;
	}
	
//	// JWT 토큰에서 인증 정보 조회
//    public Authentication getAuthentication(String token) {
//        UserDetails userDetails = userDetailsService.loadUserByUsername(this.getUserPk(token));
//        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
//    }
	
	
	// token에서 회원 정보 조회
	public Map<String, Object> getTokeninfo(String token) {
		Jws<Claims> claims = null;
        try {
            claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
        } catch (final Exception e) {
            throw new RuntimeException();
        }
        logger.info("claims : " + claims);
        return claims.getBody();
	}
	
	// Request의 Header에서 token 조회
    public String resolveToken(HttpServletRequest request) {
        return request.getHeader("Authorization");
    }
	
	// token 유효성 검사 및 만료일자 확인
	public boolean validateToken(String jwtToken) {
		try {
			Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(jwtToken);
			return !claims.getBody().getExpiration().before(new Date());
		} catch (SignatureException e) {
			logger.error("Invalid JWT signature");
		} catch (MalformedJwtException e) {
			logger.error("Invalid JWT token");
		} catch (ExpiredJwtException e) {
			logger.error("Expired JWT token"); // 유효기간 만료 
		} catch (UnsupportedJwtException e) {
			logger.error("Unsupported JWT token"); 
		} catch (IllegalArgumentException e) {
			logger.error("JWT claims string is empty");
		}

		return false;
	}

}
