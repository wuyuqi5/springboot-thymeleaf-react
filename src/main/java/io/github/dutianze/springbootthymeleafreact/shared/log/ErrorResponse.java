package io.github.dutianze.springbootthymeleafreact.shared.log;

import io.github.dutianze.springbootthymeleafreact.shared.exception.ErrorCode;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class ErrorResponse {

  private final String code;
  private final String message;
  private final LocalDateTime timestamp = LocalDateTime.now();

  public ErrorResponse(ErrorCode errorCode) {
    this.code = errorCode.getCode();
    this.message = errorCode.getMessage();
  }

  public ErrorResponse(ErrorCode errorCode, String message) {
    this.code = errorCode.getCode();
    this.message = message;
  }
}
