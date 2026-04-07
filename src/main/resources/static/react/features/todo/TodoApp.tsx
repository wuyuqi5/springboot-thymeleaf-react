import { startTransition, useEffect, useState } from "react";

type Priority = "High" | "Medium" | "Low";

type TodoItem = {
  id: number;
  title: string;
  description: string;
  priority: Priority;
  done: boolean;
  createdAt: string;
};

type TodoCreatePayload = {
  title: string;
  description: string;
  priority: Priority;
};

export function TodoApp() {
  const [todos, setTodos] = useState<TodoItem[]>([]);
  const [title, setTitle] = useState("");
  const [description, setDescription] = useState("");
  const [priority, setPriority] = useState<Priority>("Medium");
  const [loading, setLoading] = useState(true);
  const [errorMessage, setErrorMessage] = useState("");

  const openCount = todos.filter((todo) => !todo.done).length;
  const doneCount = todos.filter((todo) => todo.done).length;
  const highPriorityCount = todos.filter(
    (todo) => !todo.done && todo.priority === "High",
  ).length;

  useEffect(() => {
    void loadTodos();
  }, []);

  async function loadTodos(showLoading = true) {
    if (showLoading) {
      setLoading(true);
    }
    setErrorMessage("");
    try {
      const response = await fetch("/api/todos", {
        headers: {
          Accept: "application/json",
        },
      });
      if (!response.ok) {
        throw new Error("Failed to load todos");
      }
      const data: TodoItem[] = await response.json();
      startTransition(() => {
        setTodos(data);
      });
    } catch {
      setErrorMessage("Failed to load tasks.");
    } finally {
      if (showLoading) {
        setLoading(false);
      }
    }
  }

  async function createTodo() {
    const normalizedTitle = title.trim();
    const normalizedDescription = description.trim();
    if (!normalizedTitle) {
      return;
    }

    await postTodo("/api/todos", {
      title: normalizedTitle,
      description: normalizedDescription,
      priority,
    });
    setTitle("");
    setDescription("");
    setPriority("Medium");
  }

  async function toggleTodo(id: number) {
    await sendMutation(`/api/todos/${id}/toggle`, "PATCH");
  }

  async function deleteTodo(id: number) {
    await sendMutation(`/api/todos/${id}`, "DELETE");
  }

  async function postTodo(url: string, payload: TodoCreatePayload) {
    setErrorMessage("");
    try {
      const response = await fetch(url, {
        method: "POST",
        headers: {
          Accept: "application/json",
          "Content-Type": "application/json",
        },
        body: JSON.stringify(payload),
      });
      if (!response.ok) {
        throw new Error("Failed to create todo");
      }
      await loadTodos(false);
    } catch {
      setErrorMessage("Failed to save task.");
    }
  }

  async function sendMutation(url: string, method: "PATCH" | "DELETE") {
    setErrorMessage("");
    try {
      const response = await fetch(url, {
        method,
        headers: {
          Accept: "application/json",
        },
      });
      if (!response.ok) {
        throw new Error("Failed to update todo");
      }
      await loadTodos(false);
    } catch {
      setErrorMessage("Failed to update task.");
    }
  }

  return (
    <div className="app-content-panel p-4 sm:p-6">
      <div className="mx-auto max-w-3xl">
        <div className="mb-6">
          <div>
            <h1 className="text-2xl font-semibold tracking-[-0.03em] text-[#2e342f]">
              My tasks
            </h1>
            <p className="mt-1 text-sm text-[#6f8397]">
              The same todo flow rendered with React and loaded from the API.
            </p>
          </div>
        </div>

        <div className="rounded-[24px] border border-black/6 bg-white p-4 shadow-[0_12px_28px_rgba(57,65,58,0.05)]">
          <input
            value={title}
            onChange={(event) => setTitle(event.target.value)}
            onKeyDown={(event) => {
              if (event.key === "Enter") {
                event.preventDefault();
                void createTodo();
              }
            }}
            type="text"
            placeholder="Add a task"
            className="w-full border-0 bg-transparent px-1 py-2 text-base text-[#2e342f] outline-none placeholder:text-[#8a98a5]"
          />
          <div className="mt-3 grid gap-3 border-t border-black/6 pt-3 sm:grid-cols-[1fr_140px_auto]">
            <input
              value={description}
              onChange={(event) => setDescription(event.target.value)}
              type="text"
              placeholder="Details"
              className="app-input !rounded-[14px] !px-3 !py-2.5 !text-sm !shadow-none"
            />
            <select
              value={priority}
              onChange={(event) => setPriority(event.target.value as Priority)}
              className="app-input !rounded-[14px] !px-3 !py-2.5 !text-sm !shadow-none"
            >
              <option value="High">High</option>
              <option value="Medium">Medium</option>
              <option value="Low">Low</option>
            </select>
            <button
              type="button"
              onClick={() => void createTodo()}
              className="app-primary-btn !min-w-24 !px-4 !py-2.5"
            >
              Add
            </button>
          </div>
        </div>

        {errorMessage ? (
          <div className="mt-4 rounded-[18px] border border-[rgba(183,42,42,0.16)] bg-[var(--color-danger-bg)] px-4 py-3 text-sm text-[var(--color-danger)]">
            {errorMessage}
          </div>
        ) : null}

        <section className="mt-5">
          <div className="mb-4 flex flex-wrap items-center gap-2 text-sm text-[#6f8397]">
            <span className="rounded-full bg-white px-3 py-1 shadow-[0_6px_18px_rgba(57,65,58,0.04)]">
              {openCount} open
            </span>
            <span className="rounded-full bg-white px-3 py-1 shadow-[0_6px_18px_rgba(57,65,58,0.04)]">
              {doneCount} done
            </span>
            <span className="rounded-full bg-white px-3 py-1 shadow-[0_6px_18px_rgba(57,65,58,0.04)]">
              {highPriorityCount} high priority
            </span>
          </div>

          <div className="overflow-hidden rounded-[24px] border border-black/6 bg-white shadow-[0_16px_34px_rgba(57,65,58,0.05)]">
            {loading ? (
              <div className="px-5 py-10 text-center text-sm text-[#6f8397]">
                Loading tasks...
              </div>
            ) : todos.length === 0 ? (
              <div className="px-5 py-10 text-center text-sm text-[#6f8397]">
                No tasks yet.
              </div>
            ) : (
              todos.map((todo) => (
                <article
                  key={todo.id}
                  className="app-todo-row flex items-start gap-3 border-t border-black/6 px-4 py-4 first:border-t-0 sm:px-5"
                >
                  <button
                    type="button"
                    onClick={() => void toggleTodo(todo.id)}
                    className={`app-todo-toggle mt-0.5 flex h-5 w-5 shrink-0 items-center justify-center rounded-full border transition ${
                      todo.done
                        ? "is-done border-[#ff2442] bg-[#ff2442] text-white"
                        : "border-[#b7c2cb] bg-white text-transparent hover:border-[#ff2442]"
                    }`}
                  >
                    <svg
                      viewBox="0 0 20 20"
                      className="app-todo-check h-3.5 w-3.5 fill-current"
                    >
                      <path d="M7.6 13.2 4.7 10.3l-1.1 1.1 4 4 8-8-1.1-1.1z" />
                    </svg>
                  </button>

                  <div className="min-w-0 flex-1">
                    <div className="flex items-center gap-2">
                      <h3
                        className={`text-[15px] font-medium text-[#2e342f] ${
                          todo.done ? "line-through text-[#90a0ad]" : ""
                        }`}
                      >
                        {todo.title}
                      </h3>
                      <span
                        className={`rounded-full px-2 py-0.5 text-[10px] font-semibold uppercase tracking-[0.14em] ${
                          todo.priority === "High"
                            ? "bg-[#fff0f1] text-[#ff2442]"
                            : todo.priority === "Low"
                              ? "bg-[#f4f4f5] text-[#666]"
                              : "bg-[#fff5e8] text-[#c97710]"
                        }`}
                      >
                        {todo.priority}
                      </span>
                    </div>
                    {todo.description ? (
                      <p
                        className={`mt-1 text-sm text-[#6f8397] ${
                          todo.done ? "text-[#a8b3bc]" : ""
                        }`}
                      >
                        {todo.description}
                      </p>
                    ) : null}
                  </div>

                  <button
                    type="button"
                    onClick={() => void deleteTodo(todo.id)}
                    className="shrink-0 text-sm text-[#8b98a4] transition hover:text-[#ff2442]"
                  >
                    Delete
                  </button>
                </article>
              ))
            )}
          </div>
        </section>
      </div>
    </div>
  );
}
