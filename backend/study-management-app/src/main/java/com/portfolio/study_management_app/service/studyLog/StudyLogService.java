package com.portfolio.study_management_app.service.studyLog;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.portfolio.study_management_app.dto.studyLog.CreateStudyLogRequsetDto;
import com.portfolio.study_management_app.dto.studyLog.StudyLogResponseDto;
import com.portfolio.study_management_app.entity.category.Category;
import com.portfolio.study_management_app.entity.studyLog.StudyLog;
import com.portfolio.study_management_app.entity.user.User;
import com.portfolio.study_management_app.exception.ValidationException;
import com.portfolio.study_management_app.repository.category.CategoryRepository;
import com.portfolio.study_management_app.repository.studyLog.StudyLogRepository;
import com.portfolio.study_management_app.repository.user.UserRepository;

@Service
public class StudyLogService {
  private UserRepository userRepository;
  private CategoryRepository categoryRepository;
  private StudyLogRepository studyLogRepository;

  public StudyLogService(UserRepository userRepository, CategoryRepository categoryRepository,
      StudyLogRepository studyLogRepository) {
    this.userRepository = userRepository;
    this.categoryRepository = categoryRepository;
    this.studyLogRepository = studyLogRepository;
  }

  public StudyLogResponseDto createStudyLog(CreateStudyLogRequsetDto req) {
    // トークンからユーザー取得
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    Long userId = (Long) authentication.getPrincipal();

    User user = userRepository.findById(userId).orElseThrow();

    Category category = categoryRepository.findById(req.categoryId())
        .orElseThrow(() -> new ValidationException("データの作成に失敗しました。"));

    // 他ユーザーのカテゴリであった場合エラー
    if (category.getUser() != user) {
      throw new ValidationException("データの作成に失敗しました。");
    }

    //開始時間が終了時間より後の場合エラー
    if(!req.endTime().isAfter(req.startTime())) {
      throw new ValidationException("データの作成に失敗しました。");
    }

    // StudyLog作成
    StudyLog studyLog = new StudyLog(req.startTime(), req.endTime(), req.studySeconds(), req.memo(), user, category);

    StudyLog savedStudyLog = studyLogRepository.save(studyLog);

    return new StudyLogResponseDto(
        savedStudyLog.getStudyLogId(),
        savedStudyLog.getCategory().getCategoryName(),
        savedStudyLog.getStartTime(),
        savedStudyLog.getEndTime(),
        savedStudyLog.getStudySeconds(),
        savedStudyLog.getMemo());
  }

}
