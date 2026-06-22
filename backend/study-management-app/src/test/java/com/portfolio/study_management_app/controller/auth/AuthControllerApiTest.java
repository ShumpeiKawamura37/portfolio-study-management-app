package com.portfolio.study_management_app.controller.auth;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import com.portfolio.study_management_app.entity.user.User;
import com.portfolio.study_management_app.repository.user.UserRepository;

import jakarta.transaction.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class AuthControllerApiTest {
  @Autowired
  MockMvc mockMvc;
  @Autowired
  private UserRepository userRepository;
  @Autowired
  private PasswordEncoder passwordEncoder;

  @Test
  @DisplayName("正常系: ログイン成功")
  void authenticate_success() throws Exception {

    User user = new User(
        "テスト",
        "test@example.com",
        passwordEncoder.encode("Password123"));

    userRepository.save(user);

    String json = """
        {
            "email":"test@example.com",
            "password":"Password123"
        }
        """;

    mockMvc.perform(
        post("/api/auth/login")
            .contentType(
                org.springframework.http.MediaType.APPLICATION_JSON)
            .content(json))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status").value("SUCCESS"))
        .andExpect(jsonPath("$.data.token").exists());
  }

  @Test
  @DisplayName("異常系: ユーザーが見つからなければログイン失敗")
  void user_not_found() throws Exception {

    String json = """
        {
            "email":"test@example.com",
            "password":"Password123"
        }
        """;

    mockMvc.perform(
        post("/api/auth/login")
            .contentType(
                org.springframework.http.MediaType.APPLICATION_JSON)
            .content(json))
        .andExpect(status().isUnauthorized());
  }

  @Test
  @DisplayName("異常系: ユーザーが削除済みならログイン失敗")
  void deleted_user() throws Exception {

    User user = new User(
        "テスト",
        "test@example.com",
        passwordEncoder.encode("Password123"));

    user.setStatus(false);

    userRepository.save(user);

    String json = """
        {
            "email":"test@example.com",
            "password":"Password123"
        }
        """;

    mockMvc.perform(
        post("/api/auth/login")
            .contentType(
                org.springframework.http.MediaType.APPLICATION_JSON)
            .content(json))
        .andExpect(status().isUnauthorized());
  }

  @Test
  @DisplayName("異常系: パスワードが一致しなければログイン失敗")
  void wrong_password() throws Exception {

    User user = new User(
        "テスト",
        "test@example.com",
        passwordEncoder.encode("Password123"));

    userRepository.save(user);

    String json = """
        {
            "email":"test@example.com",
            "password":"WrongPassword"
        }
        """;

    mockMvc.perform(
        post("/api/auth/login")
            .contentType(org.springframework.http.MediaType.APPLICATION_JSON).content(json))
        .andExpect(status().isUnauthorized());
  }
}
