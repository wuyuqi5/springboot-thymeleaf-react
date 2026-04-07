package io.github.dutianze.springbootthymeleafreact.modules.todo.controller;

import io.github.dutianze.springbootthymeleafreact.modules.todo.service.TodoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class JqueryTodoController {

  private final TodoService todoService;

  @GetMapping("/jquery-todo")
  public String page(Model model) {
    model.addAttribute("todos", todoService.findAll());
    model.addAttribute("openCount", todoService.openCount());
    model.addAttribute("doneCount", todoService.doneCount());
    model.addAttribute("highPriorityCount", todoService.highPriorityCount());
    return "jquery-todo/index";
  }
}
