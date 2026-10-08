package com.portfolio.study_management_app.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UpdateUserRequestDto(
    @NotBlank
    @Size(max = 100)
    @Pattern(regexp = "^[ぁ-んァ-ヶ一-龯a-zA-Z]+(?: [ぁ-んァ-ヶ一-龯a-zA-Z]+)*$", message = "入力できない文字が含まれています。")
    String username,

    @NotBlank(message = "メールアドレスを入力してください。")
    @Size(max = 255, message = "メールアドレスは255文字以内で入力してください。")
    @Email (message = "メールアドレスの形式が正しくありません。")
    String email,

    @NotBlank(message = "パスワードを入力してください。")
    @Size(min = 8, message = "パスワードは8文字以上で入力してください。")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)\\S+$", message = "パスワードは半角英大文字・半角英小文字・半角数字をそれぞれ1文字以上含む必要があります。")
    String password
) {}