package io.github.dutianze.springbootthymeleafreact.shared.config;

import io.github.dutianze.springbootthymeleafreact.shared.log.TraceIdUtil;
import io.github.dutianze.springbootthymeleafreact.shared.util.AuthUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NullMarked;
import org.slf4j.MDC;
import org.springframework.boot.security.autoconfigure.web.servlet.PathRequest;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@Configuration
@NullMarked
public class RequestTracingFilter extends OncePerRequestFilter {

  private static final String TRACE_HEADER_NAME = "Trace-Id";

  @Override
  protected void doFilterInternal(HttpServletRequest request,
      HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {
    long start = System.currentTimeMillis();
    String traceId = TraceIdUtil.generate();
    String userName = AuthUtils.getLoginUserName();
    try {
      response.addHeader(TRACE_HEADER_NAME, traceId);

      MDC.put("traceId", traceId);

      filterChain.doFilter(request, response);
    } finally {
      long duration = System.currentTimeMillis() - start;
      int status = response.getStatus();
      log.atInfo()
          .addKeyValue("username", userName)
          .addKeyValue("http_method", request.getMethod())
          .addKeyValue("http_path", request.getRequestURI())
          .addKeyValue("http_status", status)
          .addKeyValue("duration_ms", duration)
          .log("HTTP request completed");
      MDC.clear();
    }
  }

  @Override
  protected boolean shouldNotFilter(HttpServletRequest request) {
    return PathRequest.toStaticResources().atCommonLocations().matches(request);
  }
}
