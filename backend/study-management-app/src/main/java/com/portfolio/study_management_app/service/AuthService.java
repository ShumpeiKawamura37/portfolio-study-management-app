package com.portfolio.study_management_app.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.portfolio.study_management_app.dto.auth.LoginRequestDto;
import com.portfolio.study_management_app.dto.auth.LoginResponseDto;
import com.portfolio.study_management_app.entity.user.User;
import com.portfolio.study_management_app.exception.AuthenticationException;
import com.portfolio.study_management_app.repository.user.UserRepository;
import com.portfolio.study_management_app.security.JwtProvider;

@Service
public class AuthService {
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtProvider jwtProvider;

  public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtProvider jwtProvider) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
    this.jwtProvider = jwtProvider;
  }

  public LoginResponseDto authenticate(LoginRequestDto req) {
    User user = userRepository.findByEmail(req.email());

    // userなしor削除済みユーザーを取得した場合
    if(user == null || user.isStatus() == false) {
      throw new AuthenticationException("メールアドレスまたはパスワードが正しくありません。");
    }
    
    if(!passwordEncoder.matches(req.password(), user.getPassword())) {
      throw new AuthenticationException("メールアドレスまたはパスワードが正しくありません。");
    }

    String token = jwtProvider.generateToken(user.getUserId());

    return new LoginResponseDto(token);
  }
}
