package com.portfolio.study_management_app.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.portfolio.study_management_app.dto.common.ApiResponseDto;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(ValidationException.class)
  public ResponseEntity<ApiResponseDto<Void>> handleValidationException(ValidationException e) {
    ApiResponseDto<Void> res = new ApiResponseDto<>("ERROR", null, e.getMessage());
    return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
  }
  @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ApiResponseDto<Void>> handleAuthException(AuthenticationException e) {
    ApiResponseDto<Void> res = new ApiResponseDto<>("ERROR", null, e.getMessage());
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(res);
  }

  @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<ApiResponseDto<Void>> handleInvalidTokenException(InvalidTokenException e) {
    ApiResponseDto<Void> res = new ApiResponseDto<>("ERROR", null, e.getMessage());
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(res);
  }
}


