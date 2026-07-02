package com.portfolio.study_management_app.dto.studyLog;

public record AnalyticsResponseDto(
  Integer totalStudySeconds,
  Integer studyDayCount,
  Integer averageStudySeconds,
  String CategoryNameLongestStudied,
  Double studyDayRate,
  Integer studyStreak
) {} 