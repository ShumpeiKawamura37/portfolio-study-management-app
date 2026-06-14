package com.portfolio.study_management_app.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class UserControllerApiTest {
  @Autowired private MockMvc mockMvc;

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
    ).andExpect(status().isOk());
  }
}
