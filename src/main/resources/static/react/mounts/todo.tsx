import { createRoot } from "react-dom/client";
import { TodoApp } from "../features/todo/TodoApp.tsx";

const rootElement = document.getElementById("todo-react-app");

if (rootElement) {
  createRoot(rootElement).render(<TodoApp />);
}
