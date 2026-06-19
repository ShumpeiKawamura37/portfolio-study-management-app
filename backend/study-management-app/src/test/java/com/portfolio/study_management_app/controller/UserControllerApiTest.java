package com.portfolio.study_management_app.controller;

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
  @Autowired private MockMvc mockMvc;
  @Autowired private PasswordEncoder passwordEncoder;
  @Autowired private UserRepository userRepository;
  @Autowired private JwtProvider jwtProvider;

  @Test
  @DisplayName("正常系: 新規ユーザーならUser登録成功")
  void createUSer_success() throws Exception {
    // CreateUserRequestDto req = new CreateUserRequestDto("test@example.com", "password123");

    String json = """
    {
      "email":"test@example.com",
      "password": "password123"
    } 
    """;
    mockMvc.perform(
      post("/api/user")
        .with(csrf())
        .contentType(MediaType.APPLICATION_JSON)
        .content(json)
    ).andExpect(status().isOk())
      .andExpect(jsonPath("$.data")
        .isEmpty());
  }

  @Test
  @DisplayName("正常系: User更新成功")
  void UpdateUser_success() throws Exception {
    User user = new User(
      "test",
      "test@example.com",
      passwordEncoder.encode("Password123")
    );

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
        .content(json)
      ).andExpect(status().isOk())
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
      updatedUser.getPassword())
    );

    
  }
}
