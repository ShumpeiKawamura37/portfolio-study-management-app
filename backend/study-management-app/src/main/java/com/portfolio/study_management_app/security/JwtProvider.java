package com.portfolio.study_management_app.security;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;

@Component
public class JwtProvider {
  @Value("${jwt.secret}")
  private String secret;
  
  private SecretKey secretKey;

  @Value("${jwt.expiration}")
  private Long expiration;

  @PostConstruct
  public void setKey() {
    secretKey = Keys.hmacShaKeyFor(secret.getBytes());
  }

  private Claims getClaims(String token) {
    return Jwts.parser()
      .verifyWith(secretKey)
      .build()
      .parseSignedClaims(token)
      .getPayload();
  }

  public String generateToken(Long userId) {
    return Jwts.builder()
      .subject(userId.toString())
      .issuedAt(new Date())
      .expiration(new Date(System.currentTimeMillis() + expiration))
      .signWith(secretKey)
      .compact();
  }

  public boolean validateToken(String token) {
    try {
      getClaims(token);
      return true;
    } catch( Exception e) {
      return false;
    }
  }

  public Long getUserId(String token) {
    try {
      return Long.parseLong(getClaims(token).getSubject());
    } catch (NumberFormatException e) {
      throw new JwtException("Invalid userId format");
    } catch (Exception e) {
      return null;
    }
  }
}
