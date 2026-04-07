package io.github.dutianze.springbootthymeleafreact.shared.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordValidator implements ConstraintValidator<ValidPassword, String> {

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    // Let @NotBlank handle empty/null values and skip optional password fields.
    if (value == null || value.isBlank()) {
      return true;
    }

    context.disableDefaultConstraintViolation();

    // Length check (8 to 20 characters)
    if (value.length() < 8 || value.length() > 20) {
      context.buildConstraintViolationWithTemplate(
              "Password must be between 8 and 20 characters long")
          .addConstraintViolation();
      return false;
    }

    // Disallow reserved characters (< and >)
    if (value.contains("<") || value.contains(">")) {
      context.buildConstraintViolationWithTemplate(
              "Password contains unsupported characters (< or >)")
          .addConstraintViolation();
      return false;
    }

    // Require at least one letter, one digit, and one symbol.
    boolean hasLetter = value.chars().anyMatch(Character::isLetter);
    boolean hasDigit = value.chars().anyMatch(Character::isDigit);
    boolean hasSymbol = value.chars().anyMatch(c -> !Character.isLetterOrDigit(c));

    if (!hasLetter || !hasDigit || !hasSymbol) {
      context.buildConstraintViolationWithTemplate(
              "Password must include at least one letter, one number, and one symbol")
          .addConstraintViolation();
      return false;
    }

    return true;
  }
}
