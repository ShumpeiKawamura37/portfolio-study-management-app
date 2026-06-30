package com.portfolio.study_management_app.controller.studyLog;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.portfolio.study_management_app.entity.category.Category;
import com.portfolio.study_management_app.entity.user.User;
import com.portfolio.study_management_app.repository.category.CategoryRepository;
import com.portfolio.study_management_app.repository.user.UserRepository;
import com.portfolio.study_management_app.security.JwtProvider;

import jakarta.transaction.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class StudyLogControllerApiTest {
  @Autowired MockMvc mockMvc;
  @Autowired UserRepository userRepository;
  @Autowired CategoryRepository categoryRepository;
  @Autowired JwtProvider jwtProvider;

  @Test
  @DisplayName("正常系: StudyLog登録成功")
  void createStudyLog_success() throws Exception {
    // 認証セット
    User user = new User("test", "test@example.com", "Password123");
    User savedUser = userRepository.save(user);
    String token = jwtProvider.generateToken(savedUser.getUserId());

    //カテゴリセット
    Category category = new Category("test", savedUser, null);
    Category savedCategory = categoryRepository.save(category);

    // リクエスト
    String json = """
          {
            "startTime":"2000-01-01T00:00:00",
            "endTime":"2000-01-01T00:10:00",
            "studySeconds":10,
            "memo":"test",
            "categoryId": %d
          }
        """.formatted(savedCategory.getCategoryId());

    mockMvc.perform(
      post("/api/studyLog")
        .header("Authorization", "Bearer " + token)
        .with(csrf())
        .contentType(MediaType.APPLICATION_JSON)
        .content(json))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.data.studyLogId").exists())
      .andExpect(jsonPath("$.data.categoryName").value(savedCategory.getCategoryName()))
      .andExpect(jsonPath("$.data.startTime").value("2000-01-01T00:00:00"))
      .andExpect(jsonPath("$.data.endTime").value("2000-01-01T00:10:00"))
      .andExpect(jsonPath("$.data.memo").value("test"));
  }

  @Test
  @DisplayName("異常系: カテゴリが見つからなければStudyLog登録失敗")
  void createStudylog_faild_category_not_found() throws Exception {
    // 認証セット
    User user = new User("test", "test@example.com", "Password123");
    User savedUser = userRepository.save(user);
    String token = jwtProvider.generateToken(savedUser.getUserId());

     // リクエスト
    String json = """
          {
            "startTime":"2000-01-01T00:00:00",
            "endTime":"2000-01-01T00:10:00",
            "studySeconds":10,
            "memo":"test",
            "categoryId": "1L"
          }
        """;
    mockMvc.perform(
        post("/api/studyLog")
            .header("Authorization", "Bearer " + token)
            .with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
            .andExpect(status().isBadRequest());
  }

  @Test
  @DisplayName("異常系: 別ユーザー所有のカテゴリであった場合StudyLog登録失敗")
  void createStudyLog_faild_category_is_not_own() throws Exception{
     // 認証セット
    User user = new User("test", "test@example.com", "Password123");
    User savedUser = userRepository.save(user);
    String token = jwtProvider.generateToken(savedUser.getUserId());

     //前提条件セット
    User another = new User("another", "another@example.com", "Password123");
    userRepository.save(another);
    another.setUserId(2L);
    Category category = new Category("test", another, null);
    Category savedCategory = categoryRepository.save(category);

         // リクエスト
    String json = """
          {
            "startTime":"2000-01-01T00:00:00",
            "endTime":"2000-01-01T00:10:00",
            "studySeconds":10,
            "memo":"test",
            "categoryId": %d
          }
        """.formatted(savedCategory.getCategoryId());
    mockMvc.perform(
        post("/api/studyLog")
            .header("Authorization", "Bearer " + token)
            .with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
            .andExpect(status().isBadRequest());
  }
  @Test
  @DisplayName("異常系: 開始時間が終了時間より後であった場合StudyLog登録失敗")
  void createStudyLog_faild_startTime_later_than_endTime() throws Exception{
     // 認証セット
    User user = new User("test", "test@example.com", "Password123");
    User savedUser = userRepository.save(user);
    String token = jwtProvider.generateToken(savedUser.getUserId());

     // リクエスト準備
    Category category = new Category("test", user, null);
    categoryRepository.save(category);

    String json = """
        {
          "startTime":"2000-01-01T00:10:00",
          "endTime":"2000-01-01T00:00:00",
          "studySeconds":10,
          "memo":"test",
          "categoryId": %d
        }
      """.formatted(category.getCategoryId());

    mockMvc.perform(
      post("/api/studyLog")
          .header("Authorization", "Bearer " + token)
          .with(csrf())
          .contentType(MediaType.APPLICATION_JSON)
          .content(json))
          .andExpect(status().isBadRequest());
  }
}
