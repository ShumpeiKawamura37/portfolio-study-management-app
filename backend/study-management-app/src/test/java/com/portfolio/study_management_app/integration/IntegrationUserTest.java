package com.portfolio.study_management_app.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.portfolio.study_management_app.dto.user.CreateUserRequestDto;
import com.portfolio.study_management_app.entity.user.User;
import com.portfolio.study_management_app.exception.ValidationException;
import com.portfolio.study_management_app.repository.user.UserRepository;
import com.portfolio.study_management_app.service.UserService;

@SpringBootTest
@Transactional
public class IntegrationUserTest {
  @Autowired UserService userService;
  @Autowired UserRepository userRepository;

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
}
