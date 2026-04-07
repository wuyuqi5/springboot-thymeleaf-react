package io.github.dutianze.springbootthymeleafreact.modules.todo.dto;

import io.github.dutianze.springbootthymeleafreact.modules.todo.model.Todo;
import java.time.LocalDateTime;

public record TodoItemDto(
    long id,
    String title,
    String description,
    String priority,
    boolean done,
    LocalDateTime createdAt
) {

  public static TodoItemDto from(Todo todo) {
    return new TodoItemDto(
        todo.getId(),
        todo.getTitle(),
        todo.getDescription(),
        todo.getPriority(),
        todo.isDone(),
        todo.getCreatedAt()
    );
  }
}
