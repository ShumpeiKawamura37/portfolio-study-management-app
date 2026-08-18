package com.portfolio.study_management_app.dto.analytics;

import java.time.LocalDateTime;

public record CategoryAnalyticsResponseDto (
  Long categoryId,
  int totalStudySeconds,
  LocalDateTime firstTimeStudied,
  LocalDateTime lastTimeStudied,
  double percentageOfTotal,
  Double percentageOfParentCategory
) {}
