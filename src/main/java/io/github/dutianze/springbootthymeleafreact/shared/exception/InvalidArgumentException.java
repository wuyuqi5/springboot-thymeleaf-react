package io.github.dutianze.springbootthymeleafreact.shared.exception;

import lombok.Getter;

@Getter
public class InvalidArgumentException extends BizException {

  private final String parameterName;
  private final Object parameterValue;

  public InvalidArgumentException(ErrorCode errorCode, String parameterName,
      Object parameterValue) {
    super(errorCode);
    this.parameterName = parameterName;
    this.parameterValue = parameterValue;
  }

  public InvalidArgumentException(
      ErrorCode errorCode,
      String parameterName,
      Object parameterValue,
      Throwable cause
  ) {
    super(errorCode, cause);
    this.parameterName = parameterName;
    this.parameterValue = parameterValue;
  }
}
