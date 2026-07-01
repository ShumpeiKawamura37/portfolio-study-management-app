package com.portfolio.study_management_app.repository.studyLog;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.portfolio.study_management_app.entity.category.Category;
import com.portfolio.study_management_app.entity.studyLog.StudyLog;
import com.portfolio.study_management_app.entity.user.User;
import com.portfolio.study_management_app.repository.category.CategoryRepository;
import com.portfolio.study_management_app.repository.user.UserRepository;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class StudyLogRepositoryTest {
  @Autowired UserRepository userRepository;
  @Autowired CategoryRepository categoryRepository;
  @Autowired StudyLogRepository studyLogRepository;

  @Test
  @DisplayName("正常系: userIdと時間指定で任意の日付をstartTimeとするList<StudyLog>を取得")
  void findByUserUserIdAndStartTimeGreaterThanEqualAndStartTimeLessThan_success() {
    //前提条件をセット
    User user = new User(
      "山田太郎",
      "test@example.com",
      "Password123"
    );
    userRepository.save(user);

    Category category = new Category(
      "test",
      user, 
      null
    );
    categoryRepository.save(category);

    StudyLog studyLog = new StudyLog(
        LocalDateTime.of(2000, 1, 1, 0, 0),
        LocalDateTime.of(2000, 1, 1, 0, 10),
        10, 
        "test",
        user, 
        category);
    studyLogRepository.save(studyLog);

    // 実行
    List<StudyLog> result = studyLogRepository.findByUserUserIdAndStartTimeGreaterThanEqualAndStartTimeLessThan(user.getUserId(),  LocalDateTime.of(2000, 1, 1, 0, 0),  LocalDateTime.of(2000, 1, 1, 23, 59));

    StudyLog foundStudyLog = result.get(0);
    //データ検証
    assertNotNull(foundStudyLog);
    assertEquals(studyLog.getStudyLogId(), foundStudyLog.getStudyLogId());
  }
}
