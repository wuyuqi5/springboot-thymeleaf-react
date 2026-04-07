package io.github.dutianze.springbootthymeleafreact.modules.dashboard.controller;

import io.github.dutianze.springbootthymeleafreact.modules.todo.service.TodoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
@RequiredArgsConstructor
public class HomeController {

  private final TodoService todoService;

  @GetMapping
  public String index(Model model) {
    long openCount = todoService.openCount();
    long doneCount = todoService.doneCount();
    long highPriorityCount = todoService.highPriorityCount();
    long totalCount = openCount + doneCount;

    model.addAttribute("openCount", openCount);
    model.addAttribute("doneCount", doneCount);
    model.addAttribute("highPriorityCount", highPriorityCount);
    model.addAttribute("totalCount", totalCount);
    model.addAttribute("openRatio", ratio(openCount, totalCount));
    model.addAttribute("doneRatio", ratio(doneCount, totalCount));
    model.addAttribute("highPriorityRatio", ratio(highPriorityCount, totalCount));
    return "index";
  }

  private int ratio(long value, long total) {
    if (total == 0) {
      return 0;
    }
    return (int) ((value * 100) / total);
  }
}
