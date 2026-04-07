package io.github.dutianze.springbootthymeleafreact.shared.util;

import io.github.dutianze.springbootthymeleafreact.modules.account.modal.User;
import io.github.dutianze.springbootthymeleafreact.modules.auth.model.LoginUserDetail;
import io.github.dutianze.springbootthymeleafreact.shared.exception.BizException;
import io.github.dutianze.springbootthymeleafreact.shared.exception.ErrorCode;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class AuthUtils {

  public static User getLoginUser() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    if (authentication == null || !authentication.isAuthenticated()) {
      throw new BizException(ErrorCode.AUTH_LOGIN_FAILED);
    }

    if (authentication.getPrincipal() instanceof LoginUserDetail detail) {
      return detail.user();
    }

    throw new BizException(ErrorCode.AUTH_LOGIN_FAILED);
  }

  public static String getLoginUserName() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    if (authentication == null || !authentication.isAuthenticated()) {
      return "anonymous";
    }

    if (authentication.getPrincipal() instanceof LoginUserDetail detail) {
      return detail.user().getUsername();
    }

    return "anonymous";
  }

  public static String getLoginUserDisplayName() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    if (authentication == null || !authentication.isAuthenticated()) {
      return "";
    }

    if (authentication.getPrincipal() instanceof LoginUserDetail(User user)) {
      return user.getName();
    }

    return "";
  }

  public static boolean isLoggedIn() {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    return auth != null
        && auth.isAuthenticated()
        && !(auth instanceof AnonymousAuthenticationToken);
  }
}
