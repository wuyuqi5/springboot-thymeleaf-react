package io.github.dutianze.springbootthymeleafreact.modules.todo.controller;

import io.github.dutianze.springbootthymeleafreact.modules.todo.dto.TodoCreateCommand;
import io.github.dutianze.springbootthymeleafreact.modules.todo.dto.TodoItemDto;
import io.github.dutianze.springbootthymeleafreact.modules.todo.service.TodoService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/todos")
@RequiredArgsConstructor
public class TodoApiController {

  private final TodoService todoService;

  @GetMapping
  public List<TodoItemDto> list() {
    return todoService.findAll();
  }

  @PostMapping
  public TodoItemDto create(@Valid @RequestBody TodoCreateCommand command) {
    return todoService.add(command.title(), command.description(), command.priority());
  }

  @PatchMapping("/{id}/toggle")
  public TodoItemDto toggle(@PathVariable long id) {
    return todoService.toggle(id);
  }

  @DeleteMapping("/{id}")
  public void delete(@PathVariable long id) {
    todoService.delete(id);
  }
}
