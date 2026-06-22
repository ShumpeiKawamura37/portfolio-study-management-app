package com.portfolio.study_management_app.controller.user;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.portfolio.study_management_app.dto.common.ApiResponseDto;
import com.portfolio.study_management_app.dto.user.CreateUserRequestDto;
import com.portfolio.study_management_app.dto.user.UpdateUserRequestDto;
import com.portfolio.study_management_app.dto.user.UserResponseDto;
import com.portfolio.study_management_app.service.user.UserService;

@RestController
@RequestMapping("/api/user")
public class UserController {
  private final UserService userService;

  public UserController(UserService userService) {
    this.userService = userService;
  }
  @PostMapping
  public ApiResponseDto<Void> createUser(@RequestBody CreateUserRequestDto req) {
    userService.createUser(req);
    return new ApiResponseDto<>("SUCCESS", null, null);
  }

  @PutMapping
  public ApiResponseDto<UserResponseDto> updateUser(@RequestBody UpdateUserRequestDto req) {
    UserResponseDto res = userService.updateUser(req);
    return new ApiResponseDto<>("SUCCESS", res, null);
  }

  @DeleteMapping
  public ApiResponseDto<Void> deleteUser() {
    userService.deleteUser();
    return new ApiResponseDto<>("SUCCESS", null, null);
  }
}
