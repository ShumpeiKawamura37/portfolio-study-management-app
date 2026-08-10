package com.portfolio.study_management_app.service.studyLog;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.portfolio.study_management_app.dto.studyLog.AnalyticsResponseDto;
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

  public StudyLogService(
      UserRepository userRepository,
      CategoryRepository categoryRepository,
      StudyLogRepository studyLogRepository) {
    this.userRepository = userRepository;
    this.categoryRepository = categoryRepository;
    this.studyLogRepository = studyLogRepository;
  }

  // StudyLogをStudyLogResponseDtoに変換
  private StudyLogResponseDto toDto(StudyLog studyLog) {

    return new StudyLogResponseDto(
        studyLog.getStudyLogId(),
        studyLog.getCategory().getCategoryName(),
        studyLog.getStartTime(),
        studyLog.getEndTime(),
        studyLog.getStudySeconds(),
        studyLog.getMemo());
  }

  // getAnalytics用

  // 合計時間を算出
  private Integer calculationTotalStudySeconds(List<StudyLog> studyLogs) {
    return studyLogs.stream()
        .mapToInt(StudyLog::getStudySeconds)
        .sum();
  }

  // 合計日数を算出
  private Integer calculationTotalStudyDays(List<StudyLog> studyLogs) {
    return (int) studyLogs.stream()
        .map(studyLog -> studyLog.getStartTime().toLocalDate())
        .distinct()
        .count();
  }

  // 学習時間の平均を算出
  private Integer calculationAverageStudySeconds(Integer totalStudySeconds, Integer totalStudyDays) {
    return totalStudySeconds / totalStudyDays;
  }

  // 最も学習時間の多いカテゴリ名を返す
  private String findCategoryNameLongestStudySeconds(List<StudyLog> studyLogs) {
    Map<Category, Integer> studySecondsByCategory = new HashMap<>();

    for (StudyLog studyLog : studyLogs) {
      Category category = studyLog.getCategory();

      // 一番親の要素を取り出す
      while (category.getParentCategory() != null) {
        category = category.getParentCategory();
      }
      // 学習時間をmapに格納
      studySecondsByCategory.merge(category, studyLog.getStudySeconds(), Integer::sum);
    }

    Category maxCategory = null;
    int maxStudySeconds = 0;

    // 最大値を探して格納
    for (Category category : studySecondsByCategory.keySet()) {

      int studySeconds = studySecondsByCategory.get(category);

      if (studySeconds > maxStudySeconds) {
        maxStudySeconds = studySeconds;
        maxCategory = category;
      }
    }
    return maxCategory.getCategoryName();
  }

  // 学習率を算出
  public Double calculationStudyDayRate(LocalDateTime createdAt, Integer totalStudyDays) {
    LocalDate createdDate = createdAt.toLocalDate();
    LocalDate currentDate = LocalDate.now();
    Integer totalDays = (int) (currentDate.toEpochDay() - createdDate.toEpochDay()) + 1;
    return (double) totalStudyDays / totalDays * 100;
  }

  // 連続学習日数を算出
  public Integer calculationStudyStreak(List<StudyLog> studyLogs) {
    List<LocalDate> studyDates = new ArrayList<>();
    int streak = 0;

    for (StudyLog studyLog : studyLogs) {
      LocalDate studyDate = studyLog.getStartTime().toLocalDate();

      if (!studyDates.contains(studyDate)) {
        studyDates.add(studyDate);
      }
    }
    studyDates.sort(Comparator.reverseOrder());

    LocalDate expectedDate = LocalDate.now();

    for (LocalDate studyDate : studyDates) {
      if (studyDate.equals(expectedDate)) {
        streak++;
        expectedDate = expectedDate.minusDays(1);
      }  else {
        break;
      }
    }
    return streak;
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

    // 開始時間が終了時間より後の場合エラー
    if (!req.endTime().isAfter(req.startTime())) {
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

  // ユーザーの学習記録一覧を取得
  public List<StudyLogResponseDto> getStudyLog() {
        // トークンからユーザー取得
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    Long userId = (Long) authentication.getPrincipal();

    List<StudyLog> studyLogs = studyLogRepository.findByUserUserId(userId);

    return studyLogs.stream().map(studyLog -> this.toDto(studyLog)).toList();
  }

  // 日別の学習記録を取得
  public List<StudyLogResponseDto> getStudyLogByDate(LocalDate date) {

    // トークンからユーザー取得
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    Long userId = (Long) authentication.getPrincipal();

    // 指定日の00:00:00~翌日00:00:00を定義
    LocalDateTime start = date.atStartOfDay();
    LocalDateTime end = date.plusDays(1).atStartOfDay();

    // 指定日の00:00:00~23:59:59で計測開始したStudyLogを取得
    List<StudyLog> res = studyLogRepository.findByUserUserIdAndStartTimeGreaterThanEqualAndStartTimeLessThan(userId,
        start, end);

    // List<StudyLog>をList<StudyLogResponseDto>に変換して返す
    return res.stream().map((studyLog) -> {
      return this.toDto(studyLog);
    }).toList();
  }

  public AnalyticsResponseDto getAnalytics() {
    // トークンからユーザー取得
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    Long userId = (Long) authentication.getPrincipal();

    User user = userRepository.findById(userId).orElseThrow();

    List<StudyLog> studyLogs = studyLogRepository.findByUserUserId(userId);

    if (studyLogs.isEmpty()) {
      return null;
    }

    //合計時間
    Integer totalStudySeconds = this.calculationTotalStudySeconds(studyLogs);
    //合計学習日数
    Integer totalStudyDays = this.calculationTotalStudyDays(studyLogs);
    //平均学習時間
    Integer averageStudySeconds = this.calculationAverageStudySeconds(totalStudySeconds, totalStudyDays);
    //最も学習時間の多いカテゴリ名
    String categoryNameLongestStudied = this.findCategoryNameLongestStudySeconds(studyLogs);
    //学習率
    Double studyDayRate = this.calculationStudyDayRate(user.getCreatedAt(), totalStudyDays);
    //連続学習日数
    Integer studyStreak = this.calculationStudyStreak(studyLogs);

    return new AnalyticsResponseDto(
        totalStudySeconds,
        totalStudyDays,
        averageStudySeconds,
        categoryNameLongestStudied,
        studyDayRate,
        studyStreak
    );
  }
}
