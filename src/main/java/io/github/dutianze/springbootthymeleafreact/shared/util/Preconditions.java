package io.github.dutianze.springbootthymeleafreact.shared.util;

import io.github.dutianze.springbootthymeleafreact.shared.exception.ErrorCode;
import io.github.dutianze.springbootthymeleafreact.shared.exception.InvalidArgumentException;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import org.springframework.util.StringUtils;

public final class Preconditions {

  private Preconditions() {
  }

  public static void requireText(String name, String value) {
    if (!StringUtils.hasText(value)) {
      throw missingArgument(name, value);
    }
  }

  public static void requireText(String name, CharSequence value) {
    if (!StringUtils.hasText(value)) {
      throw missingArgument(name, value);
    }
  }

  public static <T> T requireNotNull(String name, T value) {
    if (value == null) {
      throw missingArgument(name, null);
    }
    return value;
  }

  public static <T> T requireNotNull(T value,
      Supplier<? extends RuntimeException> exceptionSupplier) {
    if (value == null) {
      throw exceptionSupplier.get();
    }
    return value;
  }

  public static <T> List<T> requireNotEmpty(String name, List<T> value) {
    if (value == null || value.isEmpty()) {
      throw missingArgument(name, value);
    }
    return value;
  }

  public static <K, V> Map<K, V> requireNotEmpty(String name, Map<K, V> value) {
    if (value == null || value.isEmpty()) {
      throw missingArgument(name, value);
    }
    return value;
  }

  public static <T> T[] requireNotEmpty(String name, T[] value) {
    if (value == null || value.length == 0) {
      throw missingArgument(name, value);
    }
    return value;
  }

  public static void checkArgument(boolean expression, String name, Object value) {
    if (!expression) {
      throw invalidArgument(name, value);
    }
  }

  public static void checkArgument(boolean expression,
      Supplier<? extends RuntimeException> exceptionSupplier) {
    if (!expression) {
      throw exceptionSupplier.get();
    }
  }

  public static <T> T checkNotNull(String name, T value) {
    if (value == null) {
      throw invalidArgument(name, null);
    }
    return value;
  }

  public static <T> T checkNotNull(T value,
      Supplier<? extends RuntimeException> exceptionSupplier) {
    if (value == null) {
      throw exceptionSupplier.get();
    }
    return value;
  }

  private static InvalidArgumentException missingArgument(String name, Object value) {
    return new InvalidArgumentException(ErrorCode.COMMON_REQUIRED_ARGUMENT_MISSING, name, value);
  }

  private static InvalidArgumentException invalidArgument(String name, Object value) {
    return new InvalidArgumentException(ErrorCode.COMMON_INVALID_ARGUMENT, name, value);
  }
}
