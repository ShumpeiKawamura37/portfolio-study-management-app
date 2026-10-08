package com.portfolio.study_management_app.dto.category;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CategoryRequestDto(
  @NotNull(message = "カテゴリ名を入力してください。")
  @Size(max = 100, message = "カテゴリ名は100文字以内で入力してください。")
  String categoryName
) {}
