package com.portfolio.study_management_app.dto.category;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CategoryRequestDto(
  @NotNull
  @Size(max = 100)
  String categoryName
) {}
