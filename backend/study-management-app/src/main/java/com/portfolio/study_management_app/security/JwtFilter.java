package com.portfolio.study_management_app.security;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.portfolio.study_management_app.exception.AuthenticationException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtFilter extends OncePerRequestFilter  {
  private final JwtProvider jwtProvider;

  public JwtFilter(JwtProvider jwtProvider) {
    this.jwtProvider = jwtProvider;
  }

  @Override
  protected void doFilterInternal(
    HttpServletRequest req,
    HttpServletResponse res,
    FilterChain filterChain
  ) throws ServletException, IOException {
    String authHeader = req.getHeader("Authorization");

    if(authHeader == null) {
      filterChain.doFilter(req, res);
      return;
    }

    if(!authHeader.startsWith("Bearer ")) {
      filterChain.doFilter(req, res);
      return;
    } 

    String token = authHeader.substring(7);

    try {
      if(!jwtProvider.validateToken(token)) {
          filterChain.doFilter(req, res);
          return;
      }
      Long userId = jwtProvider.getUserId(token);
      UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(userId, null, null);

      SecurityContextHolder.getContext().setAuthentication(auth);
    } catch(AuthenticationException e) {
      SecurityContextHolder.clearContext();
    }

    filterChain.doFilter(req, res);
  }
}
