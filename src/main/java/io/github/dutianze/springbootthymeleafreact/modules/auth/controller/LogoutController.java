package io.github.dutianze.springbootthymeleafreact.modules.auth.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LogoutController {

  @GetMapping("/logout/dialog")
  public String logoutDialog() {
    return "fragments/logout-dialog :: content";
  }
}
