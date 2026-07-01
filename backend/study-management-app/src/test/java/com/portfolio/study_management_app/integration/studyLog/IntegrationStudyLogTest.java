package com.portfolio.study_management_app.integration.studyLog;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.portfolio.study_management_app.dto.studyLog.CreateStudyLogRequsetDto;
import com.portfolio.study_management_app.dto.studyLog.StudyLogResponseDto;
import com.portfolio.study_management_app.entity.category.Category;
import com.portfolio.study_management_app.entity.studyLog.StudyLog;
import com.portfolio.study_management_app.entity.user.User;
import com.portfolio.study_management_app.exception.ValidationException;
import com.portfolio.study_management_app.repository.category.CategoryRepository;
import com.portfolio.study_management_app.repository.studyLog.StudyLogRepository;
import com.portfolio.study_management_app.repository.user.UserRepository;
import com.portfolio.study_management_app.service.studyLog.StudyLogService;

import jakarta.transaction.Transactional;

@SpringBootTest
@Transactional
public class IntegrationStudyLogTest {
  @Autowired
  UserRepository userRepository;
  @Autowired
  CategoryRepository categoryRepository;
  @Autowired
  StudyLogRepository studyLogRepository;
  @Autowired
  StudyLogService studyLogService;

  @Test
  @DisplayName("正常系: StudyLog登録成功")
  void createStudyLog_success() {
    // 認証セット
    User user = new User("test", "test@exapmle.com", "Password123");

    userRepository.save(user);

    Authentication auth = new UsernamePasswordAuthenticationToken(
        user.getUserId(),
        null,
        null);

    SecurityContextHolder
        .getContext()
        .setAuthentication(auth);

    // リクエスト準備
    Category category = new Category("test", user, null);
    categoryRepository.save(category);

    CreateStudyLogRequsetDto req = new CreateStudyLogRequsetDto(
        LocalDateTime.of(2000, 1, 1, 0, 0),
        LocalDateTime.of(2000, 1, 1, 0, 10), 10,
        "memo",
        category.getCategoryId());

    // 実行
    StudyLogResponseDto result = studyLogService.createStudyLog(req);

    // データ検証
    assertNotNull(result.studyLogId());
    assertEquals(category.getCategoryName(), result.categoryName());
    assertEquals(req.startTime(), result.startTime());
    assertEquals(req.endTime(), result.endTime());
    assertEquals(req.studySeconds(), result.studySeconds());
    assertEquals(req.memo(), result.memo());
  }

  @Test
  @DisplayName("異常系: カテゴリが見つからなければStudyLog登録失敗")
  void createStudylog_faild_category_not_found() {
    // 認証セット
    User user = new User("test", "test@exapmle.com", "Password123");

    userRepository.save(user);

    Authentication auth = new UsernamePasswordAuthenticationToken(
        user.getUserId(),
        null,
        null);

    SecurityContextHolder
        .getContext()
        .setAuthentication(auth);

    // リクエスト準備
    CreateStudyLogRequsetDto req = new CreateStudyLogRequsetDto(
        LocalDateTime.of(2000, 1, 1, 0, 0),
        LocalDateTime.of(2000, 1, 1, 0, 10), 10,
        "memo",
        1L);

    // 実行・検証
    assertThrows(ValidationException.class, () -> studyLogService.createStudyLog(req));
  }

  @Test
  @DisplayName("異常系: 別ユーザー所有のカテゴリであった場合StudyLog登録失敗")
  void createStudyLog_faild_category_is_not_own() {

    // 認証セット
    User user = new User("test", "test@exapmle.com", "Password123");

    userRepository.save(user);

    Authentication auth = new UsernamePasswordAuthenticationToken(
        user.getUserId(),
        null,
        null);

    SecurityContextHolder
        .getContext()
        .setAuthentication(auth);

    // 前提条件セット
    User another = new User("another", "another@example.com", "Password123");
    userRepository.save(another);
    another.setUserId(2L);
    Category category = new Category("test", another, null);
    Category savedCategory = categoryRepository.save(category);

    // リクエスト準備
    CreateStudyLogRequsetDto req = new CreateStudyLogRequsetDto(
        LocalDateTime.of(2000, 1, 1, 0, 0),
        LocalDateTime.of(2000, 1, 1, 0, 10), 10,
        "memo",
        savedCategory.getCategoryId());

    // 実行・検証
    assertThrows(ValidationException.class, () -> studyLogService.createStudyLog(req));
  }

  @Test
  @DisplayName("異常系: 開始時間が終了時間より後であった場合StudyLog登録失敗")
  void createStudyLog_faild_startTime_later_than_endTime() {

    // 認証セット
    User user = new User("test", "test@exapmle.com", "Password123");

    userRepository.save(user);

    Authentication auth = new UsernamePasswordAuthenticationToken(
        user.getUserId(),
        null,
        null);

    SecurityContextHolder
        .getContext()
        .setAuthentication(auth);

    // リクエスト準備
    Category category = new Category("test", user, null);
    categoryRepository.save(category);

    CreateStudyLogRequsetDto req = new CreateStudyLogRequsetDto(
        LocalDateTime.of(2000, 1, 1, 0, 10),
        LocalDateTime.of(2000, 1, 1, 0, 0), 10,
        "memo",
        category.getCategoryId());

    // 実行・検証
    assertThrows(ValidationException.class, () -> studyLogService.createStudyLog(req));
  }

  @Test
  @DisplayName("正常系: 指定した日付のstartTimeを持つList<StudyLog>取得成功")
  void getStudyLogByDate_success() {
     // 認証セット
    User user = new User("test", "test@exapmle.com", "Password123");

    userRepository.save(user);

    Authentication auth = new UsernamePasswordAuthenticationToken(
        user.getUserId(),
        null,
        null);

    SecurityContextHolder
        .getContext()
        .setAuthentication(auth);
    
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
    List<StudyLogResponseDto> result = studyLogService.getStudyLogByDate(LocalDate.of(2000, 1, 1));

    // データ検証
    StudyLogResponseDto foundStudyLog = result.get(0);

    assertNotNull(result);
    assertEquals(studyLog.getStudyLogId(), foundStudyLog.studyLogId());
  }
}
