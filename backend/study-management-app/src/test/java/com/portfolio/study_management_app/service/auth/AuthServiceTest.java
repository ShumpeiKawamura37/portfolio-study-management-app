package com.portfolio.study_management_app.service.auth;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.portfolio.study_management_app.dto.auth.LoginRequestDto;
import com.portfolio.study_management_app.dto.auth.LoginResponseDto;
import com.portfolio.study_management_app.entity.user.User;
import com.portfolio.study_management_app.exception.AuthenticationException;
import com.portfolio.study_management_app.repository.user.UserRepository;
import com.portfolio.study_management_app.security.JwtProvider;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {
  @Mock private UserRepository userRepository;
  @Mock private PasswordEncoder passwordEncoder;
  @Mock private JwtProvider jwtProvider;

  @InjectMocks AuthService authService;

  @Test
  @DisplayName("正常系: 認証に成功したらLoginResponseDtoを返す")
  void authenticate_success() {
    User user = new User("テスト", "test@example.com", "password123");

    user.setUserId(1L);

    LoginRequestDto req = new LoginRequestDto("test@example.com",  "password123");

    when(userRepository.findByEmail(req.email()))
      .thenReturn(user);

    when(passwordEncoder.matches(req.password(), user.getPassword()))
      .thenReturn(true);

    when(jwtProvider.generateToken(anyLong()))
        .thenReturn("jwt-token");
    
    LoginResponseDto result = authService.authenticate(req);

    verify(passwordEncoder)
      .matches(req.password(), user.getPassword());

    verify(jwtProvider)
      .generateToken(anyLong());

    assertEquals("jwt-token", result.token());
  }

  @Test
  @DisplayName("異常系: ユーザーが見つからなければログイン失敗")
  void user_not_found() {
    LoginRequestDto req = new LoginRequestDto("test@example.com", "password123");

    when(userRepository.findByEmail(req.email()))
        .thenReturn(null);  

    AuthenticationException exception = assertThrows(
      AuthenticationException.class,
      () -> authService.authenticate(req)
    );

    assertEquals(
        "メールアドレスまたはパスワードが正しくありません。",
        exception.getMessage()
    );

    verify(userRepository)
      .findByEmail(req.email());

    verify(passwordEncoder, never())
      .matches(anyString(), anyString());
    verify(jwtProvider, never())
      .generateToken(anyLong());
  }

  @Test
  @DisplayName("異常系: 削除済みのユーザーはログイン失敗")
  void user_is_deleted() {
    User user = new User("テスト", "test@example.com", "password123");
    user.setUserId(1L);
    user.setStatus(false);

    LoginRequestDto req = new LoginRequestDto("test@example.com",  "password123");

    when(userRepository.findByEmail(req.email()))
      .thenReturn(user);
    
    AuthenticationException exception = assertThrows(
      AuthenticationException.class,
      () -> authService.authenticate(req)
    );

    assertEquals(
        "メールアドレスまたはパスワードが正しくありません。",
        exception.getMessage()
    );

    verify(userRepository)
      .findByEmail(req.email());

    verify(passwordEncoder, never())
      .matches(anyString(), anyString());
    verify(jwtProvider, never())
      .generateToken(anyLong());
  }

  @Test
  @DisplayName("異常系: パスワード不一致はログイン失敗")
  void wrong_password() {
    User user = new User("テスト", "test@example.com", "password123");
    user.setUserId(1L);

    LoginRequestDto req = new LoginRequestDto("test@example.com",  "wrongPassword");

    when(userRepository.findByEmail(req.email()))
      .thenReturn(user);

    AuthenticationException exception = assertThrows(
      AuthenticationException.class,
      () -> authService.authenticate(req)
    );

    assertEquals(
        "メールアドレスまたはパスワードが正しくありません。",
        exception.getMessage()
    );

    verify(userRepository)
      .findByEmail(req.email());

    verify(passwordEncoder)
      .matches(req.password(), user.getPassword());
    verify(jwtProvider, never())
      .generateToken(anyLong());
  }
}
