package io.github.dutianze.springbootthymeleafreact.modules.todo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ReactTodoController {

  @GetMapping("/react-todo")
  public String page() {
    return "react-todo/index";
  }
}
