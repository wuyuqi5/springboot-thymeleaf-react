package io.github.dutianze.springbootthymeleafreact.shared.log;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.IThrowableProxy;
import ch.qos.logback.classic.spi.StackTraceElementProxy;
import org.slf4j.event.KeyValuePair;
import org.springframework.boot.json.JsonWriter.Members;
import org.springframework.boot.json.JsonWriter.PairExtractor;
import org.springframework.boot.logging.structured.JsonWriterStructuredLogFormatter;
import org.springframework.boot.logging.structured.StructuredLoggingJsonMembersCustomizer;
import org.springframework.core.env.Environment;

/**
 * Structured JSON log formatter for CloudWatch.
 * <p>
 * Configure it with Spring Boot's {@code logging.structured.format.console} property.
 */
public class CloudWatchLogFormatter extends JsonWriterStructuredLogFormatter<ILoggingEvent> {

  private static final PairExtractor<KeyValuePair> KEY_VALUE_PAIR_EXTRACTOR = PairExtractor.of(
      (pair) -> pair.key,
      (pair) -> pair.value);

  private static final int MAX_STACK_FRAMES = 10;

  public CloudWatchLogFormatter(Environment env,
      StructuredLoggingJsonMembersCustomizer<?> customizer) {
    super((members) -> jsonMembers(env, members), customizer);
  }

  private static void jsonMembers(Environment env, Members<ILoggingEvent> members) {
    members.add("@timestamp", ILoggingEvent::getInstant);
    members.add("service", env.getProperty("spring.application.name"));
    members.add("environment", env.getProperty("spring.profiles.active"));
    members.add("level", ILoggingEvent::getLevel);
    members.add("thread", ILoggingEvent::getThreadName);
    members.add("message", ILoggingEvent::getFormattedMessage);
    members.addMapEntries(ILoggingEvent::getMDCPropertyMap);
    members.from(ILoggingEvent::getKeyValuePairs)
        .whenNotEmpty()
        .usingExtractedPairs(Iterable::forEach, KEY_VALUE_PAIR_EXTRACTOR);
    members.add("stack_trace", (event) -> event)
        .whenNotNull(
            iLoggingEvent -> iLoggingEvent != null ? iLoggingEvent.getThrowableProxy() : null)
        .as(CloudWatchLogFormatter::convertThrowable);
  }

  private static String convertThrowable(ILoggingEvent event) {
    IThrowableProxy proxy = event.getThrowableProxy();
    if (proxy == null) {
      return null;
    }
    var sb = new StringBuilder();
    sb.append(proxy.getClassName()).append(": ").append(proxy.getMessage());
    sb.append(stackTrace(proxy));
    IThrowableProxy cause = proxy.getCause();
    if (cause != null) {
      sb.append("\nCaused by: ").append(cause.getClassName()).append(": ")
          .append(cause.getMessage());
      sb.append(stackTrace(cause));
    }
    return sb.toString();
  }

  private static String stackTrace(IThrowableProxy ex) {
    var sb = new StringBuilder();
    StackTraceElementProxy[] frames = ex.getStackTraceElementProxyArray();
    if (frames != null) {
      int limit = Math.min(MAX_STACK_FRAMES, frames.length);
      for (int i = 0; i < limit; i++) {
        sb.append('\n').append('\t').append("at ").append(frames[i].getSTEAsString());
      }
      if (frames.length > MAX_STACK_FRAMES) {
        sb.append('\n').append('\t')
            .append("... ").append(frames.length - MAX_STACK_FRAMES).append(" more");
      }
    }
    return sb.toString();
  }
}
