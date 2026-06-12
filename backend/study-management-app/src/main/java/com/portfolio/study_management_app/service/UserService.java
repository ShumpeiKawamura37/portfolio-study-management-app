package com.portfolio.study_management_app.service;

import org.springframework.security.crypto.password.PasswordEncoder;

import com.portfolio.study_management_app.dto.user.CreateUserRequestDto;
import com.portfolio.study_management_app.dto.user.UserResponseDto;
import com.portfolio.study_management_app.entity.user.User;
import com.portfolio.study_management_app.repository.user.UserRepository;

import jakarta.validation.ValidationException;


public class UserService {
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
  }

  public void createUser(CreateUserRequestDto req) {
    if(userRepository.findByEmail(req.email()) != null) {
      throw new ValidationException("既に登録されているメールアドレスです。");
    }
    String encodedPassword = passwordEncoder.encode(req.password());
    User user = new User("no name", req.email(), encodedPassword);
    userRepository.save(user);
  }
}