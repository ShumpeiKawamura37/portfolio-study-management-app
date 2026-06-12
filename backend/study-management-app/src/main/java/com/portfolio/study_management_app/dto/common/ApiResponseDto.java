package com.portfolio.study_management_app.dto.common;

public record ApiResponseDto<T>(
  String status,
  T data,
  String message
) {}
