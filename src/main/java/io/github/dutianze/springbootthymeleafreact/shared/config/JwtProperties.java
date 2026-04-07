package io.github.dutianze.springbootthymeleafreact.shared.config;

import java.time.Duration;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.jwt")
public record JwtProperties(
    String jwtSecret,
    long accessTokenExpirationMinutes,
    long refreshTokenExpirationMinutes,
    int tokenVersion
) {

  public Duration accessTokenExpiration() {
    return Duration.ofMinutes(accessTokenExpirationMinutes);
  }

  public Duration refreshTokenExpiration() {
    return Duration.ofMinutes(refreshTokenExpirationMinutes);
  }
}
