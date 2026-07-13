package com.portfolio.study_management_app.service.user;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.portfolio.study_management_app.dto.user.CreateUserRequestDto;
import com.portfolio.study_management_app.dto.user.UpdateUserRequestDto;
import com.portfolio.study_management_app.dto.user.UserResponseDto;
import com.portfolio.study_management_app.entity.user.User;
import com.portfolio.study_management_app.exception.InvalidTokenException;
import com.portfolio.study_management_app.exception.ValidationException;
import com.portfolio.study_management_app.repository.user.UserRepository;


@Service
public class UserService {
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
  }

  public UserResponseDto getUser() {
    //認証情報からuserIdを取得
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    Long userId = (Long) authentication.getPrincipal();

    User user = userRepository.findById(userId).orElseThrow();

    return new UserResponseDto(user.getUsername(), user.getEmail());
  }

  public void createUser(CreateUserRequestDto req) {
    if(userRepository.findByEmail(req.email()) != null) {
      throw new ValidationException("既に登録されているメールアドレスです。");
    }
    String encodedPassword = passwordEncoder.encode(req.password());
    User user = new User("no name", req.email(), encodedPassword);
    userRepository.save(user);
  }

  public UserResponseDto updateUser(UpdateUserRequestDto req) {
    //認証情報からuserIdを取得
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    Long userId = (Long) authentication.getPrincipal();

    // User取得
    User user = userRepository.findById(userId)
      .orElseThrow(() -> new InvalidTokenException( "認証エラーが発生しました。再度ログインしてください。"));

    // 変更箇所があれば更新する
    if(!req.username().equals(user.getUsername())) {
      user.setUsername(req.username());
    }
    if(!req.email().equals(user.getEmail())) {
      user.setEmail(req.email());
    }
    if(!passwordEncoder.matches(req.password(), user.getPassword()) ) {
      if (req.password() != null && !req.password().isBlank()) {
        user.setPassword(passwordEncoder.encode(req.password()));
      }
    }

    User savedUser = userRepository.save(user);

    return new UserResponseDto(
      savedUser.getUsername(),
      savedUser.getEmail()
    );
  }

  public void deleteUser() {
    //認証情報からuserIdを取得
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    Long userId = (Long) authentication.getPrincipal();

    // User取得
    User user = userRepository.findById(userId)
      .orElseThrow(() -> new InvalidTokenException( "認証エラーが発生しました。再度ログインしてください。"));

    user.setStatus(false);

    userRepository.save(user);
  }
}