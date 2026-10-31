package com.portfolio.study_management_app.exception;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import com.portfolio.study_management_app.entity.user.User;
import com.portfolio.study_management_app.repository.user.UserRepository;
import com.portfolio.study_management_app.security.JwtProvider;
import com.portfolio.study_management_app.service.user.UserService;

@SpringBootTest 
@AutoConfigureMockMvc 
@Transactional 
public class GlobalExceptionHandlerTest {
  @Autowired MockMvc mockMvc;
  @Autowired UserRepository userRepository;
  @Autowired JwtProvider jwtProvider;

  // 予期せぬエラーを起こす為、UserSreviceを偽物に差し替える。
  @MockitoBean UserService userService;

  @Test
  @DisplayName ("異常系: 予期しないエラーは500を返す。エラーの中身はレスポンスに載せない。")
  void unexcpectedException() throws Exception {
    // 認証セット
    User user = new User("test", "test@exapmle.com", "Password123");
    userRepository.save(user);
    String token = jwtProvider.generateToken(user.getUserId());

    // 前提条件
    when(userService.getUser()).thenThrow(new RuntimeException("内部情報: db-password"));

    mockMvc.perform(
      get("/api/user")
        .header("Authorization", "Bearer " + token))
      .andExpect(status().isInternalServerError())
      .andExpect(jsonPath("$.status" ).value(500))
      .andExpect(jsonPath("$.detail").value("サーバーでエラーが発生しました。時間をおいて再度お試しください。"))
      .andExpect(content().string(not(containsString("db-password")))); // エラーの中身が含まれていない確認
  }
}
