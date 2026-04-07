package io.github.dutianze.springbootthymeleafreact.modules.todo.service;

import io.github.dutianze.springbootthymeleafreact.modules.todo.dto.TodoItemDto;
import io.github.dutianze.springbootthymeleafreact.modules.todo.model.Todo;
import io.github.dutianze.springbootthymeleafreact.modules.todo.repository.TodoRepository;
import io.github.dutianze.springbootthymeleafreact.shared.exception.BizException;
import io.github.dutianze.springbootthymeleafreact.shared.exception.ErrorCode;
import io.github.dutianze.springbootthymeleafreact.shared.util.Preconditions;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TodoService {

  private static final String HIGH_PRIORITY = "High";
  private static final String LOW_PRIORITY = "Low";
  private static final String MEDIUM_PRIORITY = "Medium";
  private static final String DEFAULT_DESCRIPTION = "No extra note yet.";

  private final TodoRepository todoRepository;

  public List<TodoItemDto> findAll() {
    return todoRepository.findAllByOrderByDoneAscCreatedAtDesc()
        .stream()
        .map(TodoItemDto::from)
        .toList();
  }

  @Transactional
  public TodoItemDto add(String title, String description, String priority) {
    Preconditions.requireText("title", title);

    Todo todo = new Todo(
        title.trim(),
        normalizeDescription(description),
        normalizePriority(priority)
    );
    return TodoItemDto.from(todoRepository.save(todo));
  }

  @Transactional
  public TodoItemDto toggle(long id) {
    Todo todo = todoRepository.findById(id)
        .orElseThrow(() -> new BizException(ErrorCode.TODO_NOT_FOUND));
    todo.toggleDone();
    return TodoItemDto.from(todo);
  }

  @Transactional
  public void delete(long id) {
    Todo todo = todoRepository.findById(id)
        .orElseThrow(() -> new BizException(ErrorCode.TODO_NOT_FOUND));
    todoRepository.delete(todo);
  }

  public long openCount() {
    return todoRepository.countByDoneFalse();
  }

  public long doneCount() {
    return todoRepository.countByDoneTrue();
  }

  public long highPriorityCount() {
    return todoRepository.countByDoneFalseAndPriority(HIGH_PRIORITY);
  }

  private String normalizeDescription(String description) {
    if (description == null || description.isBlank()) {
      return DEFAULT_DESCRIPTION;
    }
    return description.trim();
  }

  private String normalizePriority(String priority) {
    if (HIGH_PRIORITY.equalsIgnoreCase(priority)) {
      return HIGH_PRIORITY;
    }
    if (LOW_PRIORITY.equalsIgnoreCase(priority)) {
      return LOW_PRIORITY;
    }
    return MEDIUM_PRIORITY;
  }
}
