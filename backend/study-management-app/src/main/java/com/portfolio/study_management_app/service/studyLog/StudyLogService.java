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

import com.portfolio.study_management_app.dto.analytics.AnalyticsResponseDto;
import com.portfolio.study_management_app.dto.analytics.CategoryAnalyticsResponseDto;
import com.portfolio.study_management_app.dto.category.CategoryResponseDto;
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

  // CategoryをCategoryResponseDtoに変換
  private CategoryResponseDto categoryToDto(Category category) {

    return new CategoryResponseDto(
        category.getCategoryId(),
        category.getCategoryName(),
        category.getChildren()
            .stream()
            .map(this::categoryToDto)
            .toList()
    );
}

  // StudyLogをStudyLogResponseDtoに変換
  private StudyLogResponseDto toDto(StudyLog studyLog) {

    return new StudyLogResponseDto(
        studyLog.getStudyLogId(),
        categoryToDto(studyLog.getCategory()),
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

  // getCategoryAnalytics用

  // 合計学習時間を算出
  public int calculationTotalStudySecondsOfCategory(List<StudyLog> studyLogs) {
    return studyLogs.stream()
        .mapToInt(StudyLog::getStudySeconds)
        .sum();
  }

  //　初回学習日時を返す
  public LocalDateTime findFirstTimeStudied(List<StudyLog> studyLogs) {
    return studyLogs.stream()
        .map(StudyLog::getStartTime)
        .min(LocalDateTime::compareTo)
        .orElse(null);
  }

  // 最終学習日時を返す
  public LocalDateTime findLastTimeStudied(List<StudyLog> studyLogs) {
    return studyLogs.stream()
        .map(StudyLog:: getEndTime)
        .max(LocalDateTime:: compareTo)
        .orElse(null);
  }

  //全体に占める当該カテゴリの学習時間比率を算出
  public double calculationPercentageOfTotal(int studySecondsOfCategory, int studySecondsOfAll) {
    if(studySecondsOfAll == 0) {
      return 0.0;
    }
    return (double) studySecondsOfCategory / studySecondsOfAll;
  }

  //ある親要素以下に占める対象カテゴリの学習時間比率を算出
  public Double calculationPercentageOfDescendantCategory(int totalStudySecondsOfChild, int totalStudySecondsOfParent) {
    if (totalStudySecondsOfParent == 0) {
        return null;
    }
    return (double) totalStudySecondsOfChild / totalStudySecondsOfParent;
  }

  // 指定した要素の子孫カテゴリのリストを返す
  public List<Category> getDescendantCategoryList(List<Category> descendantCategoryList, Category targetParentCategory) {

    descendantCategoryList.add(targetParentCategory);

    if(targetParentCategory.getChildren() != null) {
      targetParentCategory.getChildren().forEach(category -> {
        descendantCategoryList.add(category);
        if(category.getChildren() != null) {
          getDescendantCategoryList(descendantCategoryList, category);
        }
      });
    }
    return descendantCategoryList;
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
        categoryToDto(studyLog.getCategory()),
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

  public CategoryAnalyticsResponseDto getCategoryAnalytics(Long categoryId, Long targetParentCategoryId) {
     // トークンからユーザー取得
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    Long userId = (Long) authentication.getPrincipal();

    // ユーザーの学習記録を全て取得
    List<StudyLog> studyLogsFilteredByUserId = studyLogRepository.findByUserUserId(userId);

    // 指定したカテゴリの学習記録リストを作成
    List<StudyLog> studyLogsFilteredByCategoryId = studyLogsFilteredByUserId.stream().filter(studyLog -> studyLog.getCategory().getCategoryId() == categoryId).toList();

    // ユーザーの合計学習時間を取得
    int totalStudySecondsOfAllCategory = calculationTotalStudySeconds(studyLogsFilteredByUserId);
    
    // カテゴリの合計学習時間
    int totalStudySecondsOfCategory = calculationTotalStudySecondsOfCategory(studyLogsFilteredByCategoryId);
    // カテゴリの初回学習日時
    LocalDateTime firstTimeStudied = findFirstTimeStudied(studyLogsFilteredByCategoryId);
    // カテゴリの最終学習日時
    LocalDateTime lastTimeStudied = findLastTimeStudied(studyLogsFilteredByCategoryId);
    // 全体に占めるカテゴリの学習時間
    Double percentageOfTotal = calculationPercentageOfTotal(totalStudySecondsOfCategory, totalStudySecondsOfAllCategory);

    // 指定した親要素から連なる子孫カテゴリに占める学習時間を先に定義(if用)
    Double percentageOfDescendantCategory = null;
    System.out.println("targetParentCategoryId: " + targetParentCategoryId);
    if(targetParentCategoryId != null) {

      Category targetParentCategory = categoryRepository.findById(targetParentCategoryId).orElseThrow();

      // 指定した親要素の子孫カテゴリリストを作成
      List<Category> descendantCategoryList = new ArrayList<>();
      getDescendantCategoryList(descendantCategoryList, targetParentCategory);

      // descendantCategoryList内のカテゴリを持つ学習記録を抽出
      List<StudyLog> studyLogsFilteredByDescendantCategories = studyLogsFilteredByUserId.stream()
        .filter(studyLog -> {
          return descendantCategoryList.stream()
            .anyMatch(descendantCategory -> {
              return studyLog.getCategory().equals(descendantCategory);
            });
        })
        .toList();

      // 抽出した学習ログの合計学習時間を算出
      int totalStudySecondsOfDescendantCategories = studyLogsFilteredByDescendantCategories.stream()
        .mapToInt(StudyLog::getStudySeconds)
        .sum();

      // 指定した親要素以下の合計学習時間に占める対象カテゴリの学習時間の割合
      percentageOfDescendantCategory = calculationPercentageOfDescendantCategory(totalStudySecondsOfCategory, totalStudySecondsOfDescendantCategories);

      System.out.println(
        "totalStudySecondsOfCategory: " + totalStudySecondsOfCategory +
        " totalStudySecondsOfDescendantCategories: " + totalStudySecondsOfDescendantCategories +
        " percentageOfDescendantCategory: " + percentageOfDescendantCategory
      );
    }


    return new CategoryAnalyticsResponseDto(categoryId, totalStudySecondsOfCategory, firstTimeStudied, lastTimeStudied, percentageOfTotal, percentageOfDescendantCategory);
  }

}
