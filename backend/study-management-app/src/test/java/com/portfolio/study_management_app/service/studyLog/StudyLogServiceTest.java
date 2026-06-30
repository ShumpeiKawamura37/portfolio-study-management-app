package com.portfolio.study_management_app.service.studyLog;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
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

@ExtendWith(MockitoExtension.class)
public class StudyLogServiceTest {
  @Mock
  UserRepository userRepository;
  @Mock
  CategoryRepository categoryRepository;
  @Mock
  StudyLogRepository studyLogRepository;
  @InjectMocks
  StudyLogService studyLogService;

  @Test
  @DisplayName("正常系: StudyLog登録成功")
  void createStudyLog_success() {
    // 認証セット
    User user = new User("test", "test@exapmle.com", "password123");

    Authentication auth = new UsernamePasswordAuthenticationToken(
        1L,
        null,
        null);

    SecurityContextHolder
        .getContext()
        .setAuthentication(auth);

    Category category = new Category("test", user, null);
    category.setCategoryId(1L);

    // リクエスト準備
    CreateStudyLogRequsetDto req = new CreateStudyLogRequsetDto(
        LocalDateTime.of(2000, 1, 1, 0, 0),
        LocalDateTime.of(2000, 1, 1, 0, 10), 10,
        "memo",
        category.getCategoryId());

    // 実行準備
    when(userRepository.findById(anyLong()))
        .thenReturn(Optional.of(user));
    when(categoryRepository.findById(anyLong()))
        .thenReturn(Optional.of(category));
    when(studyLogRepository.save(any(StudyLog.class)))
        .thenAnswer(invocation -> {
          StudyLog c = invocation.getArgument(0);
          c.setStudyLogId(1L);
          return c;
        });

    // 実行
    StudyLogResponseDto result = studyLogService.createStudyLog(req);

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
    User user = new User("test", "test@exapmle.com", "password123");

    Authentication auth = new UsernamePasswordAuthenticationToken(
        1L,
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

    // 実行準備
    when(userRepository.findById(anyLong()))
        .thenReturn(Optional.of(user));
    // 実行・検証
    assertThrows(ValidationException.class, () -> studyLogService.createStudyLog(req));

    verify(studyLogRepository, never()).save(any(StudyLog.class));
  }

  @Test
  @DisplayName("異常系: 別ユーザー所有のカテゴリであった場合StudyLog登録失敗")
  void createStudyLog_faild_category_is_not_own() {

    // 認証セット
    User user = new User("test", "test@exapmle.com", "password123");

    Authentication auth = new UsernamePasswordAuthenticationToken(
        1L,
        null,
        null);

    SecurityContextHolder
        .getContext()
        .setAuthentication(auth);

    //前提条件セット
    User another = new User("another", "another@example.com", "Password123");
    another.setUserId(2L);
    Category category = new Category("test", another, null);

     // リクエスト準備
    CreateStudyLogRequsetDto req = new CreateStudyLogRequsetDto(
        LocalDateTime.of(2000, 1, 1, 0, 0),
        LocalDateTime.of(2000, 1, 1, 0, 10), 10,
        "memo",
        1L);
    
    // 実行準備
    when(userRepository.findById(anyLong()))
        .thenReturn(Optional.of(user));
    when(categoryRepository.findById(anyLong()))
        .thenReturn(Optional.of(category));
    
    // 実行・検証
    assertThrows(ValidationException.class, () -> studyLogService.createStudyLog(req));

    verify(studyLogRepository, never()).save(any(StudyLog.class));
  }

  @Test
  @DisplayName("異常系: 開始時間が終了時間より後であった場合StudyLog登録失敗")
  void createStudyLog_faild_startTime_later_than_endTime() {
    // 認証セット
    User user = new User("test", "test@exapmle.com", "password123");

    Authentication auth = new UsernamePasswordAuthenticationToken(
        1L,
        null,
        null);

    SecurityContextHolder
        .getContext()
        .setAuthentication(auth);

    // 前提条件セット
    Category category = new Category("test", user, null);
    category.setCategoryId(1L);
      
    // リクエスト準備
    CreateStudyLogRequsetDto req = new CreateStudyLogRequsetDto(
        LocalDateTime.of(2000, 1, 1, 0, 10),
        LocalDateTime.of(2000, 1, 1, 0, 0), 10,
        "memo",
        category.getCategoryId());

    // 実行準備
    when(userRepository.findById(anyLong()))
        .thenReturn(Optional.of(user));
    when(categoryRepository.findById(anyLong()))
        .thenReturn(Optional.of(category));

    // 実行・検証
    assertThrows(ValidationException.class, () -> studyLogService.createStudyLog(req));

    verify(studyLogRepository, never()).save(any(StudyLog.class));
  }
}
