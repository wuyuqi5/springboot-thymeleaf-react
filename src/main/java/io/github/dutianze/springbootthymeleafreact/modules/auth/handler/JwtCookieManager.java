package io.github.dutianze.springbootthymeleafreact.modules.auth.handler;

import static io.github.dutianze.springbootthymeleafreact.modules.auth.constant.JwtConstants.ACCESS_TOKEN_COOKIE_NAME;
import static io.github.dutianze.springbootthymeleafreact.modules.auth.constant.JwtConstants.REFRESH_TOKEN_COOKIE_NAME;

import io.github.dutianze.springbootthymeleafreact.shared.config.JwtProperties;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
@RequiredArgsConstructor
public class JwtCookieManager {

  private final JwtProperties jwtProperties;

  public void setAccessToken(HttpServletRequest request, HttpServletResponse response,
      String token) {
    String contextPath = request.getContextPath();
    ResponseCookie cookie = ResponseCookie.from(ACCESS_TOKEN_COOKIE_NAME, token)
        .httpOnly(false)
        .secure(true)
        .path(StringUtils.hasLength(contextPath) ? contextPath : "/")
        .maxAge(jwtProperties.accessTokenExpiration().toSeconds())
        .sameSite("Lax")
        .build();
    response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
  }

  public void setRefreshToken(HttpServletRequest request, HttpServletResponse response,
      String token) {
    String contextPath = request.getContextPath();
    ResponseCookie cookie = ResponseCookie.from(REFRESH_TOKEN_COOKIE_NAME, token)
        .httpOnly(true)
        .secure(true)
        .path(StringUtils.hasLength(contextPath) ? contextPath : "/")
        .maxAge(jwtProperties.refreshTokenExpiration().toSeconds())
        .sameSite("Lax")
        .build();
    response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
  }

  public void clearAll(HttpServletRequest request, HttpServletResponse response) {
    String contextPath = request.getContextPath();
    String path = StringUtils.hasLength(contextPath) ? contextPath : "/";
    for (String name : new String[]{ACCESS_TOKEN_COOKIE_NAME, REFRESH_TOKEN_COOKIE_NAME}) {
      ResponseCookie deletion = ResponseCookie.from(name, "")
          .httpOnly(true)
          .path(path)
          .maxAge(0)
          .sameSite("Lax")
          .build();
      response.addHeader(HttpHeaders.SET_COOKIE, deletion.toString());
    }
  }
}
