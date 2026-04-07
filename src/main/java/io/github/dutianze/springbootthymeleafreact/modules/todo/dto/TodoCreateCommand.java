package io.github.dutianze.springbootthymeleafreact.modules.todo.dto;

import jakarta.validation.constraints.NotBlank;

public record TodoCreateCommand(
    @NotBlank(message = "Title is required")
    String title,
    String description,
    String priority
) {
}
