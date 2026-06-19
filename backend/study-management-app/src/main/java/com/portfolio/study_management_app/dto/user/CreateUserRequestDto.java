package com.portfolio.study_management_app.dto.user;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record CreateUserRequestDto(
  @NotBlank
  @Column(nullable = false)
  @Size(max = 255)
  @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)\\S+$")
  String email,

  @NotBlank
  @Column(nullable = false)
  @Size(min = 8)
  @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)\\S+$")
  String password
) {} 
