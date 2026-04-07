package io.github.dutianze.springbootthymeleafreact.modules.auth.filter;

import static io.github.dutianze.springbootthymeleafreact.modules.auth.constant.JwtConstants.ACCESS_TOKEN_COOKIE_NAME;
import static io.github.dutianze.springbootthymeleafreact.modules.auth.constant.JwtConstants.REFRESH_TOKEN_COOKIE_NAME;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Optional;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenExtractor {

  public String extractAccessToken(HttpServletRequest request) {
    return extractCookie(request, ACCESS_TOKEN_COOKIE_NAME);
  }

  public String extractRefreshToken(HttpServletRequest request) {
    return extractCookie(request, REFRESH_TOKEN_COOKIE_NAME);
  }

  private String extractCookie(HttpServletRequest request, String cookieName) {
    return Optional.ofNullable(request.getCookies())
        .stream()
        .flatMap(Arrays::stream)
        .filter(cookie -> cookieName.equals(cookie.getName()))
        .map(Cookie::getValue)
        .findFirst()
        .orElse(null);
  }
}
