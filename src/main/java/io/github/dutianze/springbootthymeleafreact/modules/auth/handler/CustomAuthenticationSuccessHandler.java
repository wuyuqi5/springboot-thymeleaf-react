package io.github.dutianze.springbootthymeleafreact.modules.auth.handler;

import io.github.dutianze.springbootthymeleafreact.modules.account.modal.User;
import io.github.dutianze.springbootthymeleafreact.modules.auth.model.LoginUserDetail;
import io.github.dutianze.springbootthymeleafreact.modules.auth.service.JwtService;
import io.github.dutianze.springbootthymeleafreact.shared.exception.BizException;
import io.github.dutianze.springbootthymeleafreact.shared.exception.ErrorCode;
import io.github.wimdeblauwe.htmx.spring.boot.security.HxLocationRedirectAuthenticationSuccessHandler;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.NullMarked;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@NullMarked
@Component
@RequiredArgsConstructor
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

  private final JwtService jwtService;
  private final JwtCookieManager jwtCookieManager;
  private final AuthenticationSuccessHandler delegate =
      new HxLocationRedirectAuthenticationSuccessHandler("/", true);

  @Override
  public void onAuthenticationSuccess(HttpServletRequest request,
      @NonNull HttpServletResponse response,
      Authentication authentication) throws IOException, ServletException {
    if (!(authentication.getPrincipal() instanceof LoginUserDetail(User user))) {
      throw new BizException(ErrorCode.AUTH_UNEXPECTED_PRINCIPAL);
    }

    String accessToken = jwtService.generateAccessToken(user);
    String refreshToken = jwtService.generateRefreshToken(user);

    jwtCookieManager.setAccessToken(request, response, accessToken);
    jwtCookieManager.setRefreshToken(request, response, refreshToken);

    delegate.onAuthenticationSuccess(request, response, authentication);
  }
}
