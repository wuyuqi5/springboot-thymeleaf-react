package io.github.dutianze.springbootthymeleafreact.modules.auth.controller;

import io.github.dutianze.springbootthymeleafreact.shared.util.AuthUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@Controller
@RequestMapping("login")
@RequiredArgsConstructor
public class LoginController {

  @GetMapping
  public String login(@RequestParam(required = false) String error,
      @RequestParam(required = false) String logout,
      @RequestParam(required = false) String tooManyAttempts,
      @RequestParam(required = false) String disabled,
      @RequestParam(required = false) String passwordReset,
      Model model) {
    if (AuthUtils.isLoggedIn()) {
      return "redirect:/";
    }

    if (tooManyAttempts != null) {
      model.addAttribute("tooManyAttempts", true);
    } else if (disabled != null) {
      model.addAttribute("error", "This account has been disabled");
    } else if (error != null) {
      model.addAttribute("error", "The username or password is incorrect");
    }
    if (passwordReset != null) {
      model.addAttribute("message", "Your password has been reset");
    } else if (logout != null) {
      model.addAttribute("message", "You have been logged out");
    }
    return "login/index";
  }

}
