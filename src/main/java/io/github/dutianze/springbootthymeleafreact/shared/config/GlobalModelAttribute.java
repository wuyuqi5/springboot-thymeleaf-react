package io.github.dutianze.springbootthymeleafreact.shared.config;

import io.github.dutianze.springbootthymeleafreact.shared.util.AuthUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalModelAttribute {

  @ModelAttribute("currentPath")
  public String currentPath(HttpServletRequest request) {
    return request.getRequestURI().substring(request.getContextPath().length());
  }

  @ModelAttribute("loginUserName")
  public String loginUserName() {
    return AuthUtils.getLoginUserDisplayName();
  }
}
