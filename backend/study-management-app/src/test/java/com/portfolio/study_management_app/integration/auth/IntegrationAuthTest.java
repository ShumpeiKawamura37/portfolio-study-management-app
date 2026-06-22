package com.portfolio.study_management_app.integration.auth;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.portfolio.study_management_app.dto.auth.LoginRequestDto;
import com.portfolio.study_management_app.dto.auth.LoginResponseDto;
import com.portfolio.study_management_app.entity.user.User;
import com.portfolio.study_management_app.exception.AuthenticationException;
import com.portfolio.study_management_app.repository.user.UserRepository;
import com.portfolio.study_management_app.service.auth.AuthService;

import jakarta.transaction.Transactional;

@SpringBootTest
@Transactional
public class IntegrationAuthTest {
  @Autowired AuthService authService;
  @Autowired UserRepository userRepository;
  @Autowired PasswordEncoder passwordEncoder;

  @Test
  @DisplayName("正常系: ログイン成功")
  void authenticate_success() {
    User user = new User("テスト", "test@example.com",  passwordEncoder.encode("Password123"));

    userRepository.save(user);

    LoginRequestDto req = new LoginRequestDto(
      "test@example.com",  
      "Password123"
    );

    LoginResponseDto res = authService.authenticate(req);

    assertNotNull(res.token());
  }

  @Test
  @DisplayName("異常系: ユーザーが見つからなければログイン失敗")
  void user_not_found() throws Exception {
    LoginRequestDto req = new LoginRequestDto(
      "test@example.com",  
      "password123"
    );

    AuthenticationException exception = assertThrows(
      AuthenticationException.class, 
      () -> authService.authenticate(req));
  }

  @Test
  @DisplayName("異常系: 削除済みのユーザーはログイン失敗")
  void deleted_user() {
    User user = new User(
        "テスト",
        "test@example.com",
        passwordEncoder.encode("Password123")
    );
    user.setStatus(false);

    userRepository.save(user);

    LoginRequestDto req = new LoginRequestDto(
        "test@example.com",
        "Password123"
    );

    assertThrows(
        AuthenticationException.class,
        () -> authService.authenticate(req)
    );
}

  @Test
  @DisplayName("異常系: パスワード不一致はログイン失敗")
  void wrong_password() {
    User user = new User(
        "テスト",
        "test@example.com",
        passwordEncoder.encode("Password123")
    );

    userRepository.save(user);

    LoginRequestDto req = new LoginRequestDto(
        "test@example.com",
        "WrongPassword"
    );

    assertThrows(
        AuthenticationException.class,
        () -> authService.authenticate(req)
    );
}
}
