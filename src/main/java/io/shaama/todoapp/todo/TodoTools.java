package io.shaama.todoapp.todo;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TodoTools {

    private final TodoService todoService;


    @Tool(description = "Gets all Todo items")
    public List<Todo> fetchAllTodos() {
        return todoService.getAllTodos();
    }

    @Tool(description = "Gets a Todo item by ID")
    public Optional<Todo> fetchTodoById(
            @ToolParam(description = "id for the Item")
            Long id
    ) {
        return todoService.getTodoById(id);
    }

    @Tool(description = "Gets Todo items by category")
    public List<Todo> fetchTodosByCategory(
            @ToolParam(description = "Category of the Todo items - valid values: WORK, PERSONAL, SHOPPING, HEALTH, STUDY, HOME, OTHER")
            String category
    ) {
        if (category == null || category.trim().isEmpty()) {
            return List.of();
        }
        
        String normalizedCategory = category.toUpperCase();
        if (!todoService.isValidCategory(normalizedCategory)) {
            return List.of(); // Return empty list for invalid category
        }
        
        return todoService.getTodoBy(normalizedCategory);
    }

    @Tool(description = "Creates a new Todo item")
    public Todo makeTodo(
            @ToolParam(description = "Title for the Todo")
            String title,

            @ToolParam(description = "Description for the Todo")
            String description,

            @ToolParam(description = "Categorize tasks into one of these categories: WORK, PERSONAL, SHOPPING, HEALTH, STUDY, HOME, OTHER. Based on this todo item")
            String category,

            @ToolParam(description = "Is the Todo completed?")
            boolean completed
    ) {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Title cannot be empty");
        }

        String normalizedCategory = category != null ? category.toUpperCase() : "OTHER";
        if (!todoService.isValidCategory(normalizedCategory)) {
            normalizedCategory = "OTHER";
        }

        Todo todo = Todo.builder()
                .title(title.trim())
                .description(description != null ? description.trim() : "")
                .completed(completed)
                .category(normalizedCategory)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        return todoService.createTodo(todo);
    }

    @Tool(description = "Updates an existing Todo item")
    public Optional<Todo> changeTodo(
            @ToolParam(description = "id for the Item")
            Long id,

            @ToolParam(description = "Title for the Todo")
            String title,

            @ToolParam(description = "Description for the Todo")
            String description,

            @ToolParam(description = "Categorize tasks into one of these categories: WORK, PERSONAL, SHOPPING, HEALTH, STUDY, HOME, OTHER. Based on this todo item")
            String category,

            @ToolParam(description = "Is the Todo completed?")
            boolean completed
    ) {
        if (id == null || id <= 0) {
            return Optional.empty();
        }

        return todoService.getTodoById(id).map(todo -> {
            if (title != null && !title.trim().isEmpty()) {
                todo.setTitle(title.trim());
            }
            if (description != null) {
                todo.setDescription(description.trim());
            }
            if (category != null) {
                String normalizedCategory = category.toUpperCase();
                if (todoService.isValidCategory(normalizedCategory)) {
                    todo.setCategory(normalizedCategory);
                }
            }
            todo.setCompleted(completed);
            todo.setUpdatedAt(LocalDateTime.now());
            return todoService.updateTodo(todo);
        });
    }

    @Tool(description = "Deletes a Todo item by ID")
    public boolean removeTodo(
            @ToolParam(description = "id for the Item")
            Long id
    ) {
        return todoService.getTodoById(id).map(todo -> {
            todoService.deleteTodo(id);
            return true;

        }).orElse(false);
    }

    @Tool(description = "Deletes all TODO's")
    public String removeAllTodo(
    ) {
        return todoService.deleteAllTodos()? "All Todos deleted" : "Error deleting all Todos";
    }

    @Tool(description = "Gets Todo items by completion status")
    public List<Todo> fetchTodosByStatus(
            @ToolParam(description = "true for completed todos, false for pending todos")
            boolean completed
    ) {
        return todoService.getTodosByCompleted(completed);
    }

    @Tool(description = "Gets Todo items by category and completion status")
    public List<Todo> fetchTodosByCategoryAndStatus(
            @ToolParam(description = "Category of the Todo items")
            String category,
            @ToolParam(description = "true for completed todos, false for pending todos")
            boolean completed
    ) {
        if (!todoService.isValidCategory(category)) {
            return List.of();
        }
        return todoService.getTodosByCategoryAndCompleted(category.toUpperCase(), completed);
    }

    @Tool(description = "Search Todo items by title keywords")
    public List<Todo> searchTodosByTitle(
            @ToolParam(description = "Keywords to search in todo titles")
            String titleKeywords
    ) {
        if (titleKeywords == null || titleKeywords.trim().isEmpty()) {
            return List.of();
        }
        return todoService.searchTodosByTitle(titleKeywords.trim());
    }

    @Tool(description = "Search Todo items by description keywords")
    public List<Todo> searchTodosByDescription(
            @ToolParam(description = "Keywords to search in todo descriptions")
            String descriptionKeywords
    ) {
        if (descriptionKeywords == null || descriptionKeywords.trim().isEmpty()) {
            return List.of();
        }
        return todoService.searchTodosByDescription(descriptionKeywords.trim());
    }

    @Tool(description = "Gets comprehensive statistics about todos including counts by category and completion status")
    public String getTodoStatistics() {
        List<Todo> allTodos = todoService.getAllTodos();
        
        if (allTodos.isEmpty()) {
            return "No todos found. Your todo list is empty.";
        }

        long completedCount = allTodos.stream().filter(Todo::isCompleted).count();
        long pendingCount = allTodos.size() - completedCount;

        StringBuilder stats = new StringBuilder();
        stats.append(String.format("📊 Todo Statistics:%n"));
        stats.append(String.format("Total Todos: %d%n", allTodos.size()));
        stats.append(String.format("✅ Completed: %d%n", completedCount));
        stats.append(String.format("⏳ Pending: %d%n%n", pendingCount));

        stats.append("📂 By Category:%n");
        for (String category : List.of("WORK", "PERSONAL", "SHOPPING", "HEALTH", "STUDY", "HOME", "OTHER")) {
            long categoryCount = allTodos.stream()
                    .filter(todo -> category.equals(todo.getCategory()))
                    .count();
            if (categoryCount > 0) {
                long categoryCompleted = allTodos.stream()
                        .filter(todo -> category.equals(todo.getCategory()) && todo.isCompleted())
                        .count();
                stats.append(String.format("  %s: %d total (%d completed, %d pending)%n", 
                        category, categoryCount, categoryCompleted, categoryCount - categoryCompleted));
            }
        }

        return stats.toString();
    }
}
