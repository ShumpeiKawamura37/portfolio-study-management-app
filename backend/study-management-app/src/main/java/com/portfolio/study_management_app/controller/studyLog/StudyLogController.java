package com.portfolio.study_management_app.controller.studyLog;

import java.time.LocalDate;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.portfolio.study_management_app.dto.analytics.AnalyticsResponseDto;
import com.portfolio.study_management_app.dto.analytics.CategoryAnalyticsResponseDto;
import com.portfolio.study_management_app.dto.common.ApiResponseDto;
import com.portfolio.study_management_app.dto.studyLog.CreateStudyLogRequsetDto;
import com.portfolio.study_management_app.dto.studyLog.StudyLogResponseDto;
import com.portfolio.study_management_app.service.studyLog.StudyLogService;

@RestController
@RequestMapping("/api/studyLog")
public class StudyLogController {
  private final StudyLogService studyLogService;

  public StudyLogController(StudyLogService studyLogService) {
    this.studyLogService = studyLogService;
  }
  @PostMapping
  public ApiResponseDto<StudyLogResponseDto> ctreateStudyLog(@RequestBody CreateStudyLogRequsetDto req) {
    StudyLogResponseDto res = studyLogService.createStudyLog(req);
    return new ApiResponseDto<>("SUCCESS", res, null);
  }

  @GetMapping
  public ApiResponseDto<List<StudyLogResponseDto>> getStudyLog() {
    List<StudyLogResponseDto> res = studyLogService.getStudyLog();
    return new ApiResponseDto<>("SUCCESS", res, null);
  }

  @GetMapping("/date/{date}")
  public ApiResponseDto<List<StudyLogResponseDto>> getStudyLogByDate(@PathVariable LocalDate date) {
    List<StudyLogResponseDto> res = studyLogService.getStudyLogByDate(date);
    return new ApiResponseDto<>("SUCCESS", res, null);
  }

  @GetMapping("/analytics")
  public ApiResponseDto<AnalyticsResponseDto> getAnalytics() {
    AnalyticsResponseDto res = studyLogService.getAnalytics();
    return new ApiResponseDto<>("SUCCESS", res, null);
  }

  @GetMapping("/analytics/{categoryId}")
  public ApiResponseDto<CategoryAnalyticsResponseDto> getCategoryAnalytics(@PathVariable Long categoryId) {
    CategoryAnalyticsResponseDto res = studyLogService.getCategoryAnalytics(categoryId);
    return new ApiResponseDto<>("SUCCESS", res, null);
  }
}
