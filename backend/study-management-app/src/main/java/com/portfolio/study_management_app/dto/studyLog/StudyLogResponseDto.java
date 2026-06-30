package com.portfolio.study_management_app.dto.studyLog;

import java.time.LocalDateTime;

public record StudyLogResponseDto(
  Long studyLogId,
  String categoryName,
  LocalDateTime startTime,
  LocalDateTime endTime,
  Integer studySeconds,
  String memo
) {}
