package com.portfolio.study_management_app.security;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.portfolio.study_management_app.dto.common.ApiResponseDto;


import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component 
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
  private final ObjectMapper objectMapper;

  public  CustomAuthenticationEntryPoint(ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
  }

  @Override
  public void commence (
    HttpServletRequest req,
    HttpServletResponse res,
    AuthenticationException authenticationException
  ) throws IOException, ServletException  {
    ApiResponseDto<Void> body = new ApiResponseDto<>(
      "ERROR",
      null,
      "認証エラーが発生しました。再度ログインしてください。"
    );

    res.setStatus(HttpStatus.UNAUTHORIZED.value());
    res.setContentType(MediaType.APPLICATION_JSON_VALUE);
    res.setCharacterEncoding("UTF-8");

    objectMapper.writeValue(res.getWriter(), body);
  }
}
