package io.github.dutianze.springbootthymeleafreact.modules.todo.controller;

import io.github.dutianze.springbootthymeleafreact.modules.todo.service.TodoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/todo")
@RequiredArgsConstructor
public class TodoController {

  private final TodoService todoService;

  @GetMapping
  public String page(Model model) {
    populate(model);
    return "todo/index";
  }

  @PostMapping("/items")
  public String add(
      @RequestParam String title,
      @RequestParam(required = false) String description,
      @RequestParam(defaultValue = "Medium") String priority,
      Model model
  ) {
    todoService.add(title, description, priority);
    populate(model);
    return "todo/fragments :: board";
  }

  @PostMapping("/items/{id}/toggle")
  public String toggle(@PathVariable long id, Model model) {
    todoService.toggle(id);
    populate(model);
    return "todo/fragments :: board";
  }

  @PostMapping("/items/{id}/delete")
  public String delete(@PathVariable long id, Model model) {
    todoService.delete(id);
    populate(model);
    return "todo/fragments :: board";
  }

  private void populate(Model model) {
    model.addAttribute("todos", todoService.findAll());
    model.addAttribute("openCount", todoService.openCount());
    model.addAttribute("doneCount", todoService.doneCount());
    model.addAttribute("highPriorityCount", todoService.highPriorityCount());
  }
}
