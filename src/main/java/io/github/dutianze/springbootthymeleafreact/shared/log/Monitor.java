package io.github.dutianze.springbootthymeleafreact.shared.log;

import io.github.dutianze.springbootthymeleafreact.shared.exception.ErrorCode;
import java.util.Objects;
import org.slf4j.helpers.MessageFormatter;

public class Monitor {

  private static final String LOG_PREFIX = "[MONITOR_LOG]";

  public static ErrorCodeStep builder() {
    return new Builder();
  }

  public interface ErrorCodeStep {

    SeverityStep errorCode(ErrorCode errorCode);
  }

  public interface SeverityStep {

    ContextStep severity(Severity severity);
  }

  public interface ContextStep {

    OptionalSteps context(String template, Object... args);
  }

  public interface OptionalSteps {

    OptionalSteps reason(String reason);

    OptionalSteps solution(String solution);

    String build();
  }

  private static class Builder implements ErrorCodeStep, SeverityStep, ContextStep, OptionalSteps {

    private ErrorCode errorCode;
    private Severity severity;
    private String context;
    private String reason;
    private String solution;

    @Override
    public SeverityStep errorCode(ErrorCode errorCode) {
      this.errorCode = Objects.requireNonNull(errorCode, "errorCode must not be null");
      return this;
    }

    @Override
    public ContextStep severity(Severity severity) {
      this.severity = Objects.requireNonNull(severity, "severity must not be null");
      return this;
    }

    @Override
    public OptionalSteps context(String template, Object... args) {
      this.context = Objects.requireNonNull(template, "context must not be null");
      this.context = MessageFormatter.arrayFormat(template, args).getMessage();
      return this;
    }

    @Override
    public OptionalSteps reason(String reason) {
      this.reason = reason;
      return this;
    }

    @Override
    public OptionalSteps solution(String solution) {
      this.solution = solution;
      return this;
    }

    @Override
    public String build() {
      Objects.requireNonNull(errorCode, "errorCode is required");
      Objects.requireNonNull(severity, "severity is required");
      Objects.requireNonNull(context, "context is required");

      StringBuilder logBuilder = new StringBuilder(LOG_PREFIX);
      logBuilder.append(" code:").append(errorCode.getCode());
      logBuilder.append(" severity:").append(severity.name());

      if (context != null && !context.isBlank()) {
        logBuilder.append(" context:[").append(context).append("]");
      }
      if (reason != null && !reason.isBlank()) {
        logBuilder.append(" reason:").append(reason);
      }
      if (solution != null && !solution.isBlank()) {
        logBuilder.append(" solution:").append(solution);
      }
      return logBuilder.toString();
    }
  }
}
