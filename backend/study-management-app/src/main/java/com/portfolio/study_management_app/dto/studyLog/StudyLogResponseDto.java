package com.portfolio.study_management_app.dto.studyLog;

import java.time.LocalDateTime;

import com.portfolio.study_management_app.dto.category.CategoryResponseDto;

public record StudyLogResponseDto(
  Long studyLogId,
  CategoryResponseDto category,
  LocalDateTime startTime,
  LocalDateTime endTime,
  Integer studySeconds,
  String memo
) {}
