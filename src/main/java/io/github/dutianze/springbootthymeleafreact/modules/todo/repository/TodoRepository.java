package io.github.dutianze.springbootthymeleafreact.modules.todo.repository;

import io.github.dutianze.springbootthymeleafreact.modules.todo.model.Todo;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TodoRepository extends JpaRepository<Todo, Long> {

  List<Todo> findAllByOrderByDoneAscCreatedAtDesc();

  long countByDoneFalse();

  long countByDoneTrue();

  long countByDoneFalseAndPriority(String priority);
}
