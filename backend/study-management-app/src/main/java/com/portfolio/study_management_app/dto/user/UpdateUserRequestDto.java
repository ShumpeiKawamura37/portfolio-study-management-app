package com.portfolio.study_management_app.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UpdateUserRequestDto(
    @NotBlank
    @Size(max = 100)
    @Pattern(regexp = "^[ぁ-んァ-ヶ一-龯a-zA-Z]+(?: [ぁ-んァ-ヶ一-龯a-zA-Z]+)*$")
    String username,

    @NotBlank
    @Size(max = 255)
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)\\S+$")
    String email,

    @NotBlank
    @Size(min = 8)
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)\\S+$")
    String password
) {}