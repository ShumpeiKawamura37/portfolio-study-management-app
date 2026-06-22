package com.portfolio.study_management_app.dto.category;

import java.util.List;

public record CategoryResponseDto(
  Long categoryId,
  String categoryName,
  List<CategoryResponseDto> children
) {} 