package com.portfolio.study_management_app.controller.studyLog;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import java.time.LocalDateTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.portfolio.study_management_app.entity.category.Category;
import com.portfolio.study_management_app.entity.studyLog.StudyLog;
import com.portfolio.study_management_app.entity.user.User;
import com.portfolio.study_management_app.repository.category.CategoryRepository;
import com.portfolio.study_management_app.repository.studyLog.StudyLogRepository;
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
  @Autowired StudyLogRepository studyLogRepository;
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
      .andDo(print())
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.data.studyLogId").exists())
      .andExpect(jsonPath("$.data.category.categoryId").value(savedCategory.getCategoryId()))
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

  @Test
  @DisplayName("正常系: StudyLog取得成功")
  void getStudyLog_success() throws Exception  {
     // 認証セット
    User user = new User("test", "test@example.com", "Password123");
    User savedUser = userRepository.save(user);
    String token = jwtProvider.generateToken(savedUser.getUserId());

     // リクエスト準備
    Category category = new Category("test", user, null);
    categoryRepository.save(category);
    StudyLog studyLog = new StudyLog(
        LocalDateTime.of(2000, 1, 1, 0, 0),
        LocalDateTime.of(2000, 1, 1, 0, 10),
        10, 
        "test",
        user, 
        category);
    studyLogRepository.save(studyLog);

    //実行
    mockMvc.perform(
      get("/api/studyLog")
          .header("Authorization", "Bearer " + token)
          .with(csrf())
          .contentType(MediaType.APPLICATION_JSON))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.data[0].studyLogId").value(studyLog.getStudyLogId()));
  }

  @Test
  @DisplayName("正常系: 指定した日付のstartTimeを持つList<StudyLog>取得成功")
  void getStudyLogByDate_success() throws Exception {
     // 認証セット
    User user = new User("test", "test@example.com", "Password123");
    User savedUser = userRepository.save(user);
    String token = jwtProvider.generateToken(savedUser.getUserId());

     // リクエスト準備
    Category category = new Category("test", user, null);
    categoryRepository.save(category);
    StudyLog studyLog = new StudyLog(
        LocalDateTime.of(2000, 1, 1, 0, 0),
        LocalDateTime.of(2000, 1, 1, 0, 10),
        10, 
        "test",
        user, 
        category);
    studyLogRepository.save(studyLog);

    //実行
    mockMvc.perform(
      get("/api/studyLog/date/{date}", "2000-01-01")
          .header("Authorization", "Bearer " + token)
          .with(csrf())
          .contentType(MediaType.APPLICATION_JSON))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.data[0].studyLogId").value(studyLog.getStudyLogId()));
  }

  @Test
  @DisplayName("正常系: 学習分析を取得成功")
  void getAnalytics_success() throws Exception {
     // 認証セット
    User user = new User("test", "test@example.com", "Password123");
    User savedUser = userRepository.save(user);
    savedUser.setCreatedAt(LocalDateTime.of(2000, 1, 1, 0, 0));
    String token = jwtProvider.generateToken(savedUser.getUserId());

    // 前提条件セット
    Category category = new Category("test", savedUser, null);
    Category childCategory = new Category("child", savedUser, category);
    categoryRepository.save(category);
    categoryRepository.save(childCategory);

    StudyLog studyLog1 = new StudyLog(
        LocalDateTime.of(2000, 1, 1, 0, 0),
        LocalDateTime.of(2000, 1, 1, 0, 10),
        10, 
        "test",
        savedUser, 
        category);
    StudyLog studyLog2 = new StudyLog(
        LocalDateTime.of(2000, 1, 2, 0, 0),
        LocalDateTime.of(2000, 1, 2, 0, 20),
        20,
        "test",
        savedUser,
        childCategory);
    studyLogRepository.save(studyLog1);
    studyLogRepository.save(studyLog2);

    //実行
    mockMvc.perform(
      get("/api/studyLog/analytics")
          .header("Authorization", "Bearer " + token)
          .with(csrf())
          .contentType(MediaType.APPLICATION_JSON))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.data").exists());
          
  }
}
