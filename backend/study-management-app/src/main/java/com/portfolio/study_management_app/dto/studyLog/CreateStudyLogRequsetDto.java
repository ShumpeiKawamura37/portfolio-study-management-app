package com.portfolio.study_management_app.dto.studyLog;

import java.time.LocalDateTime;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateStudyLogRequsetDto(
  @NotNull
  LocalDateTime startTime,
  @NotNull
  LocalDateTime endTime,
  @NotNull
  Integer studySeconds,
  @Size(max = 1000)
  String memo,
  @NotNull
  Long categoryId
) {}