package com.portfolio.study_management_app.controller.auth;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.portfolio.study_management_app.dto.auth.LoginRequestDto;
import com.portfolio.study_management_app.dto.auth.LoginResponseDto;
import com.portfolio.study_management_app.dto.common.ApiResponseDto;
import com.portfolio.study_management_app.service.AuthService;

@RestController
@RequestMapping("/api/auth/login")
public class AuthController {
  private final AuthService authService;
  public AuthController(AuthService authService) {
    this.authService = authService;
  }

  @PostMapping
  public ApiResponseDto<LoginResponseDto> login(@RequestBody LoginRequestDto req) {
    LoginResponseDto res = authService.authenticate(req);
    return new ApiResponseDto<>("SUCCESS", res, null);
  }
}
