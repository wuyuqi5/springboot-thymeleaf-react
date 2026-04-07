(function ($) {
  const $title = $("#jquery-todo-title");
  const $description = $("#jquery-todo-description");
  const $priority = $("#jquery-todo-priority");
  const $addButton = $("#jquery-todo-add");
  const $error = $("#jquery-todo-error");
  const $list = $("#jquery-todo-list");
  const $board = $("#jquery-todo-board");
  const $openCount = $("#jquery-open-count");
  const $doneCount = $("#jquery-done-count");
  const $highCount = $("#jquery-high-count");

  function showError(message) {
    $error.text(message).removeClass("todo-hidden");
  }

  function clearError() {
    $error.addClass("todo-hidden").text("");
  }

  function priorityClass(priority) {
    if (priority === "High") {
      return "todo-priority-high";
    }
    if (priority === "Low") {
      return "todo-priority-low";
    }
    return "todo-priority-medium";
  }

  function createTodoItem(todo) {
    const $item = $("<article>", {
      class: "todo-item",
      "data-id": todo.id,
    });
    const $toggle = $("<button>", {
      type: "button",
      class: `todo-toggle${todo.done ? " is-done" : ""}`,
      "data-action": "toggle",
    });
    const $toggleIcon = $(
      '<svg viewBox="0 0 20 20" class="todo-check"><path d="M7.6 13.2 4.7 10.3l-1.1 1.1 4 4 8-8-1.1-1.1z"></path></svg>',
    );
    const $main = $("<div>", { class: "todo-item-main" });
    const $head = $("<div>", { class: "todo-item-head" });
    const $title = $("<h3>", {
      class: `todo-item-title${todo.done ? " is-done" : ""}`,
      text: todo.title,
    });
    const $priority = $("<span>", {
      class: `todo-priority ${priorityClass(todo.priority)}`,
      text: todo.priority,
    });
    const $delete = $("<button>", {
      type: "button",
      class: "todo-delete",
      "data-action": "delete",
      text: "Delete",
    });

    $toggle.append($toggleIcon);
    $head.append($title, $priority);
    $main.append($head);

    if (todo.description) {
      $main.append(
        $("<p>", {
          class: `todo-item-description${todo.done ? " is-done" : ""}`,
          text: todo.description,
        }),
      );
    }

    return $item.append($toggle, $main, $delete);
  }

  function patchTodoList(todos) {
    const existingItems = new Map();

    $list.children("[data-id]").each(function () {
      existingItems.set(String($(this).data("id")), $(this));
    });

    if (todos.length === 0) {
      $list.empty().append($("<div>", { class: "todo-empty", text: "No tasks yet." }));
      return;
    }

    $list.children(".todo-empty").remove();

    todos.forEach(function (todo) {
      const id = String(todo.id);
      const $nextItem = createTodoItem(todo);
      const $currentItem = existingItems.get(id);

      if ($currentItem) {
        $currentItem.replaceWith($nextItem);
        existingItems.delete(id);
      } else {
        $list.append($nextItem);
      }
    });

    existingItems.forEach(function ($item) {
      $item.remove();
    });
  }

  function renderTodos(todos) {
    const openCount = todos.filter((todo) => !todo.done).length;
    const doneCount = todos.filter((todo) => todo.done).length;
    const highCount = todos.filter((todo) => !todo.done && todo.priority === "High").length;

    $openCount.text(openCount + " open");
    $doneCount.text(doneCount + " done");
    $highCount.text(highCount + " high priority");
    patchTodoList(todos);
  }

  function loadTodos() {
    clearError();
    return $.ajax({
      url: "/api/todos",
      method: "GET",
      headers: {
        Accept: "application/json",
      },
    })
      .done(renderTodos)
      .fail(function () {
        showError("Failed to load tasks.");
      });
  }

  function createTodo() {
    const title = String($title.val() ?? "").trim();
    if (!title) {
      return;
    }

    clearError();
    return $.ajax({
      url: "/api/todos",
      method: "POST",
      contentType: "application/json",
      headers: {
        Accept: "application/json",
      },
      data: JSON.stringify({
        title,
        description: $description.val(),
        priority: $priority.val(),
      }),
    })
      .done(function () {
        $title.val("");
        $description.val("");
        $priority.val("Medium");
        loadTodos();
      })
      .fail(function () {
        showError("Failed to save task.");
      });
  }

  function toggleTodo(id) {
    clearError();
    return $.ajax({
      url: `/api/todos/${id}/toggle`,
      method: "PATCH",
      headers: {
        Accept: "application/json",
      },
    })
      .done(loadTodos)
      .fail(function () {
        showError("Failed to update task.");
      });
  }

  function deleteTodo(id) {
    clearError();
    return $.ajax({
      url: `/api/todos/${id}`,
      method: "DELETE",
      headers: {
        Accept: "application/json",
      },
    })
      .done(loadTodos)
      .fail(function () {
        showError("Failed to update task.");
      });
  }

  $addButton.on("click", createTodo);
  $title.on("keydown", function (event) {
    if (event.key === "Enter") {
      event.preventDefault();
      createTodo();
    }
  });

  $list.on("click", "[data-action='toggle']", function () {
    const id = $(this).closest("[data-id]").data("id");
    toggleTodo(id);
  });

  $list.on("click", "[data-action='delete']", function () {
    const id = $(this).closest("[data-id]").data("id");
    deleteTodo(id);
  });

})(jQuery);
