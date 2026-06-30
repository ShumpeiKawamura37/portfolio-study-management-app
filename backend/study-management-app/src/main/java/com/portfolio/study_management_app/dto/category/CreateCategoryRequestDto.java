package com.portfolio.study_management_app.dto.category;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateCategoryRequestDto(
  @NotNull
  @Size(max = 100)
  String categoryName,

  Long parentCategoryId
) {} 