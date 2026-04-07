package io.github.dutianze.springbootthymeleafreact.shared.exception;

import lombok.Getter;

@Getter
public class BizException extends RuntimeException {

  private final ErrorCode errorCode;
  private final String externalCode;
  private final String externalMessage;

  public BizException(ErrorCode errorCode) {
    super(errorCode.getMessage());
    this.errorCode = errorCode;
    this.externalCode = null;
    this.externalMessage = null;
  }

  public BizException(ErrorCode errorCode, Throwable cause) {
    super(errorCode.getMessage(), cause);
    this.errorCode = errorCode;
    this.externalCode = null;
    this.externalMessage = null;
  }

  public BizException(
      ErrorCode errorCode,
      String externalCode,
      String externalMessage
  ) {
    super(buildMessage(errorCode, externalCode, externalMessage));
    this.errorCode = errorCode;
    this.externalCode = externalCode;
    this.externalMessage = externalMessage;
  }

  private static String buildMessage(
      ErrorCode errorCode,
      String externalCode,
      String externalMessage
  ) {
    if (externalCode == null && externalMessage == null) {
      return errorCode.getMessage();
    }
    return String.format(
        "%s (externalCode=%s, externalMessage=%s)",
        errorCode.getMessage(),
        externalCode,
        externalMessage
    );
  }
}
