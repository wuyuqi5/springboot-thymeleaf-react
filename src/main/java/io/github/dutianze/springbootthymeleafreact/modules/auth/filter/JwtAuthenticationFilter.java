package io.github.dutianze.springbootthymeleafreact.modules.auth.filter;

import io.github.dutianze.springbootthymeleafreact.modules.auth.handler.JwtCookieManager;
import io.github.dutianze.springbootthymeleafreact.modules.auth.model.LoginUserDetail;
import io.github.dutianze.springbootthymeleafreact.modules.auth.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private final JwtService jwtService;
  private final JwtTokenExtractor tokenExtractor;
  private final JwtCookieManager jwtCookieManager;
  private final UserDetailsService userDetailsService;

  @Override
  protected void doFilterInternal(@NonNull HttpServletRequest request,
      @NonNull HttpServletResponse response, @NonNull FilterChain filterChain)
      throws ServletException, IOException {

    String accessToken = tokenExtractor.extractAccessToken(request);
    if (jwtService.isTokenValid(accessToken) && jwtService.isTokenVersionValid(accessToken)) {
      authenticate(jwtService.buildUserDetail(accessToken), request);
      filterChain.doFilter(request, response);
      return;
    }

    String refreshToken = tokenExtractor.extractRefreshToken(request);
    if (jwtService.isTokenValid(refreshToken) && jwtService.isRefreshToken(refreshToken)
        && jwtService.isTokenVersionValid(refreshToken)) {
      LoginUserDetail userDetail = (LoginUserDetail) userDetailsService.loadUserByUsername(
          jwtService.extractUsername(refreshToken));
      jwtCookieManager.setAccessToken(request, response,
          jwtService.generateAccessToken(userDetail.user()));
      authenticate(userDetail, request);
      filterChain.doFilter(request, response);
      return;
    }

    jwtCookieManager.clearAll(request, response);
    filterChain.doFilter(request, response);
  }

  private void authenticate(LoginUserDetail userDetail, HttpServletRequest request) {
    UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
        userDetail, null, userDetail.getAuthorities());
    auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
    SecurityContextHolder.getContext().setAuthentication(auth);
  }
}
