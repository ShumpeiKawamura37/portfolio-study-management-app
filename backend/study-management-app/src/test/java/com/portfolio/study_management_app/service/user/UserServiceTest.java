package com.portfolio.study_management_app.service.user;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.portfolio.study_management_app.dto.user.CreateUserRequestDto;
import com.portfolio.study_management_app.dto.user.UpdateUserRequestDto;
import com.portfolio.study_management_app.dto.user.UserResponseDto;
import com.portfolio.study_management_app.entity.user.User;
import com.portfolio.study_management_app.exception.ValidationException;
import com.portfolio.study_management_app.repository.user.UserRepository;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
  @Mock
  UserRepository userRepository;
  @Mock
  PasswordEncoder passwordEncoder;
  @InjectMocks
  UserService userService;

  @Test
  @DisplayName("正常系: 新規ユーザーならUser登録成功")
  void createUser_success() {
    CreateUserRequestDto req = new CreateUserRequestDto(
        "test@example.com",
        "password123");

    when(userRepository.findByEmail(req.email()))
        .thenReturn(null);

    when(passwordEncoder.encode(req.password()))
        .thenReturn("encodedPassword");

    userService.createUser(req);

    ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);

    verify(userRepository).save(captor.capture());

    User savedUser = captor.getValue();

    assertEquals("no name", savedUser.getUsername());
    assertEquals("test@example.com", savedUser.getEmail());
    assertEquals("encodedPassword", savedUser.getPassword());
  }

  @Test
  @DisplayName("異常系: メールアドレス重複していれば登録失敗")
  void createUser_duplicateEmail() {
    CreateUserRequestDto req = new CreateUserRequestDto(
        "test@example.com",
        "password123");

    User user = new User("test", "test@exapmle.com", "password123");

    when(userRepository.findByEmail(req.email()))
        .thenReturn(user);

    when(userRepository.findByEmail(req.email()))
        .thenReturn(user);

    assertThrows(
        ValidationException.class,
        () -> userService.createUser(req));

    verify(userRepository, never())
        .save(any(User.class));
  }

  @Test
  @DisplayName("正常系: User更新成功")
  void updateUser_success() {
    User user = new User(
        "test",
        "test@example.com",
        "Password123");
    user.setUserId(1L);

    UpdateUserRequestDto req = new UpdateUserRequestDto("updated", "updated@example.com",
        "UpdatedPassword");

    Authentication auth = new UsernamePasswordAuthenticationToken(
        1L,
        null,
        null);

    SecurityContextHolder
        .getContext()
        .setAuthentication(auth);

    when(userRepository.findById(1L))
        .thenReturn(Optional.of(user));

    when(passwordEncoder.matches(req.password(), user.getPassword()))
        .thenReturn(false);

    when(passwordEncoder.encode(req.password()))
        .thenReturn("encodedNewPassword");

    when(userRepository.save(any(User.class)))
        .thenAnswer(invocation -> invocation.getArgument(0));

    UserResponseDto result = userService.updateUser(req);

    ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);

    verify(userRepository).save(captor.capture());

    User savedUser = captor.getValue();

    assertEquals("updated", result.username());
    assertEquals("updated@example.com", result.email());
    assertEquals("encodedNewPassword", savedUser.getPassword());
  }

  @Test
  @DisplayName("正常系: User削除成功")
  void deleteUser_success() {
    User user = new User(
        "test",
        "test@example.com",
        "Password123");

    user.setUserId(1L);

    Authentication auth = new UsernamePasswordAuthenticationToken(
        1L,
        null,
        null);

    SecurityContextHolder
        .getContext()
        .setAuthentication(auth);

    when(userRepository.findById(1L))
        .thenReturn(Optional.of(user));

    when(userRepository.save(any(User.class)))
        .thenAnswer(invocation -> invocation.getArgument(0));

    userService.deleteUser();

    ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);

    verify(userRepository)
        .save(captor.capture());

    User deletedUser = captor.getValue();

    assertEquals(false, deletedUser.isStatus());
  }
}
