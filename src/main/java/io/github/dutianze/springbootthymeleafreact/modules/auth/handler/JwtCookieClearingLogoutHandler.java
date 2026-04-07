package io.github.dutianze.springbootthymeleafreact.modules.auth.handler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtCookieClearingLogoutHandler implements LogoutHandler {

  private final JwtCookieManager jwtCookieManager;

  @Override
  public void logout(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
      Authentication authentication) {
    jwtCookieManager.clearAll(request, response);
  }
}
