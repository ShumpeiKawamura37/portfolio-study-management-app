package com.portfolio.study_management_app.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

// 各エラーを拾い、ProlemDetail形式で返す。
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

  private static final Logger logger =  LoggerFactory.getLogger(GlobalExceptionHandler.class);

  // バリデーションエラー
  @ExceptionHandler(ValidationException.class)
  public ProblemDetail handleValidationException(ValidationException e) {
    return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, e.getMessage());
  }

  // ログインエラー
  @ExceptionHandler(AuthenticationException.class)
  public ProblemDetail handleAuthException(AuthenticationException e) {
    return ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED, e.getMessage());
  }

  // トークン認証エラー
  @ExceptionHandler(InvalidTokenException.class)
  public ProblemDetail handleInvalidTokenException(InvalidTokenException e) {
    return ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED, e.getMessage());
  }

  // @Valid用のバリデーションエラー
  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(
      MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
    String message = ex.getBindingResult().getAllErrors().get(0).getDefaultMessage();
    ProblemDetail problem = ProblemDetail.forStatusAndDetail(status, message);
    return ResponseEntity.status(status).body(problem);
  }

  // 重複エラー
  @ExceptionHandler(DuplicateException.class)
  public ProblemDetail handleDuplicateException(DuplicateException e) {
    return ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, e.getMessage());
  }

  //対象が見つからないエラー
  @ExceptionHandler(ResourceNotFoundException.class)
  public ProblemDetail handleResourceNotFoundException(ResourceNotFoundException e) {
    return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, e.getMessage());
  }

  @ExceptionHandler(Exception.class)
  public ProblemDetail handleUnexpectedException(Exception e) {
    logger.error("予期しないエラーが発生しました", e);
    return ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, "サーバーでエラーが発生しました。時間をおいて再度お試しください。");//e.getMessage()ではなく固定のメッセージで、エラーの中身を開示しない。
  }
}
