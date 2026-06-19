package com.portfolio.study_management_app.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import com.portfolio.study_management_app.dto.user.CreateUserRequestDto;
import com.portfolio.study_management_app.dto.user.UpdateUserRequestDto;
import com.portfolio.study_management_app.dto.user.UserResponseDto;
import com.portfolio.study_management_app.entity.user.User;
import com.portfolio.study_management_app.exception.ValidationException;
import com.portfolio.study_management_app.repository.user.UserRepository;
import com.portfolio.study_management_app.service.UserService;

@SpringBootTest
@Transactional
public class IntegrationUserTest {
  @Autowired UserService userService;
  @Autowired UserRepository userRepository;
  @Autowired PasswordEncoder passwordEncoder;

  @Test
  @DisplayName("正常系: 新規ユーザーならUser登録成功")
  void createUser_success() {
    CreateUserRequestDto req = new CreateUserRequestDto(
      "test@example.com",
      "password123"
    );
    userService.createUser(req);

    User savedUser = userRepository.findByEmail("test@example.com");
    assertNotNull(savedUser);
    assertEquals("test@example.com", savedUser.getEmail());
  }

  @Test
  @DisplayName("異常系: メールアドレス重複ならUser登録失敗")
  void createUser_duplicateEmail() {
    User user = new User(
      "test", 
      "test@example.com", 
      "Password123"
    );
    userRepository.save(user);

    CreateUserRequestDto req = new CreateUserRequestDto(
      "test@example.com",
      "Password123"
    );

    assertThrows(ValidationException.class, ()-> userService.createUser(req));
  }

  @Test
  @DisplayName("正常系: user更新成功")
  void updatedUser_success() {
    User user = new User(
      "test", 
      "test@example.com", 
      "Password123"
    );

    userRepository.save(user);

    UpdateUserRequestDto req = new UpdateUserRequestDto("updated", "updated@example.com", "UpdatedPassword");

    Authentication auth =
    new UsernamePasswordAuthenticationToken(
        user.getUserId(),
        null,
        null
    );

    SecurityContextHolder
      .getContext()
      .setAuthentication(auth);

    UserResponseDto result = userService.updateUser(req);

    User updatedUser =
      userRepository.findById(user.getUserId())
          .orElseThrow();

    assertEquals(
      "updated",
      updatedUser.getUsername()
    );

    assertEquals(
      "updated@example.com",
      updatedUser.getEmail()
    );

    assertTrue(
    passwordEncoder.matches(
        "UpdatedPassword",
        updatedUser.getPassword()
    ));

    assertEquals(
      "updated",
      result.username()
    );

    assertEquals(
      "updated@example.com",
      result.email()
    );
  }
  @Test
  @DisplayName("正常系: user削除成功")
  void deleteUser_success() {
    User user = new User("test", "test@example.com", "Password123");

    userRepository.save(user);
    
    Authentication auth =
    new UsernamePasswordAuthenticationToken(
        user.getUserId(),
        null,
        null
    );

    SecurityContextHolder
      .getContext()
      .setAuthentication(auth);
    
    userService.deleteUser();

    User deletedUser = userRepository.findByEmail(user.getEmail());

    assertEquals(false, deletedUser.isStatus());
  }
}
