package com.portfolio.study_management_app.controller.user;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import com.portfolio.study_management_app.entity.user.User;
import com.portfolio.study_management_app.repository.user.UserRepository;
import com.portfolio.study_management_app.security.JwtProvider;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class UserControllerApiTest {
  @Autowired
  private MockMvc mockMvc;
  @Autowired
  private PasswordEncoder passwordEncoder;
  @Autowired
  private UserRepository userRepository;
  @Autowired
  private JwtProvider jwtProvider;

  @Test
  @DisplayName("正常系: 新規ユーザーならUser登録成功")
  void createUSer_success() throws Exception {
    // CreateUserRequestDto req = new CreateUserRequestDto("test@example.com",
    // "Password123");

    String json = """
        {
            "email":"test@example.com",
            "password": "Password123"
        }
        """;
    mockMvc.perform(
        post("/api/user")
            .with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.data")
            .isEmpty());
  }

  @Test
  @DisplayName("正常系: User更新成功")
  void UpdateUser_success() throws Exception {
    User user = new User(
        "test",
        "test@example.com",
        passwordEncoder.encode("Password123"));

    userRepository.save(user);

    String token = jwtProvider.generateToken(user.getUserId());

    String json = """
        {
          "username": "updated",
          "email":"updated@example.com",
          "password": "UpdatedPassword123"
        }
        """;

    mockMvc.perform(
        put("/api/user")
            .header("Authorization", "Bearer " + token)
            .with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status")
            .value("SUCCESS"))
        .andExpect(jsonPath("$.data.username")
            .value("updated"))
        .andExpect(jsonPath("$.data.email")
            .value("updated@example.com"));

    User updatedUser = userRepository.findById(user.getUserId())
        .orElseThrow();

    assertEquals("updated", updatedUser.getUsername());

    assertEquals("updated@example.com", updatedUser.getEmail());

    assertTrue(passwordEncoder.matches("UpdatedPassword123",
        updatedUser.getPassword()));
  }

  @Test
  @DisplayName("正常系: User更新成功(パスワード以外)")
  void updateUser_withoutPassword_keepsPassword() throws Exception {
    User user = new User(
        "test",
        "test@example.com",
        passwordEncoder.encode("Password123"));

    userRepository.save(user);

    String token = jwtProvider.generateToken(user.getUserId());

    String updateJson = """
        {
          "username": "updated",
          "email": "test@example.com"
        }
        """;

    mockMvc.perform(
        put("/api/user")
            .header("Authorization", "Bearer " + token)
            .with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(updateJson))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.data.username").value("updated"));

    String loginJson = """
        {
          "email": "test@example.com",
          "password": "Password123"
        }
        """;

    mockMvc.perform(
        post("/api/auth/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content(loginJson))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.data.token").exists());
  }

  @Test
  @DisplayName("異常系: 他のユーザーが使っているメールアドレスには更新できない")
  void updateUser_duplicateEmail_conflict() throws Exception {
    User userA = new User("userA", "a@example.com", passwordEncoder.encode("Password123"));
    User userB = new User("userB", "b@example.com", passwordEncoder.encode("Password123"));
    userRepository.save(userA);
    userRepository.save(userB);

    String token = jwtProvider.generateToken(userB.getUserId());

    String json = """
        {
          "username": "userB",
          "email": "a@example.com"
        }
        """;

    mockMvc.perform(
        put("/api/user")
            .header("Authorization", "Bearer " + token)
            .with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
        .andExpect(status().isConflict())
        .andExpect(jsonPath("$.message").value("既に登録されているメールアドレスです。"));

    // userB のメールアドレスは変わっていない
    assertEquals("b@example.com", userRepository.findById(userB.getUserId()).orElseThrow().getEmail());
  }

  @Test
  @DisplayName("正常系: User削除成功")
  void deleteUser_success() throws Exception {
    User user = new User(
        "test",
        "test@example.com",
        passwordEncoder.encode("Password123"));

    userRepository.save(user);
    String token = jwtProvider.generateToken(user.getUserId());
    mockMvc.perform(
        delete("/api/user")
            .header("Authorization", "Bearer " + token)
            .with(csrf()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status").value("SUCCESS"));

    User deletedUser = userRepository.findByEmail(user.getEmail());

    assertEquals(false, deletedUser.isStatus());
  }
}
