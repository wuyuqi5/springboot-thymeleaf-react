package io.github.dutianze.springbootthymeleafreact.shared.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {

  // ===== Common / Validation =====
  COMMON_INVALID_ARGUMENT("APP-COMMON-VALIDATION-001", "An invalid parameter was specified"),
  COMMON_REQUIRED_ARGUMENT_MISSING("APP-COMMON-VALIDATION-002",
      "A required parameter is missing"),
  COMMON_ARGUMENT_INVALID("APP-COMMON-ARG-003", "The request parameters are invalid"),

  // ===== System =====
  SYS_NORMAL("APP-SYS-000-000", "The system is operating normally"),
  SYS_ERROR("APP-SYS-ERROR-001",
      "A system error occurred. Please wait a moment and try again."),
  SYS_API_ERROR("APP-SYS-API-001", "A communication error occurred. Please try again."),

  // ===== DB =====
  DB_CONSTRAINT_VIOLATION("APP-DB-CONSTRAINT-001", "A database constraint violation occurred"),

  // ===== Auth =====
  AUTH_LOGIN_FAILED("APP-AUTH-LOGIN-001", "Login failed"),
  AUTH_ACCESS_DENIED("APP-AUTH-ACCESS-002", "You do not have permission to access this page"),
  AUTH_REFRESH_TOKEN_NOT_ALLOWED("APP-AUTH-TOKEN-003",
      "Refresh token cannot be used for this operation"),
  AUTH_UNEXPECTED_PRINCIPAL("APP-AUTH-LOGIN-004", "Unexpected authenticated principal"),

  // ===== Account =====
  ACCOUNT_USERNAME_ALREADY_EXISTS("APP-ACCOUNT-REG-001", "This username is already in use"),
  ACCOUNT_REQUIRED_ROLE_MISSING("APP-ACCOUNT-REG-002",
      "A required account role is missing"),

  // ===== Todo =====
  TODO_NOT_FOUND("APP-TODO-001", "The requested todo item was not found");


  private final String code;
  private final String message;

  ErrorCode(String code, String message) {
    this.code = code;
    this.message = message;
  }

  @Override
  public String toString() {
    return code;
  }
}
