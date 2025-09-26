package io.shaama.todoapp.todo;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TodoService {

    private final TodoRepository todoRepository;

    public List<Todo> getAllTodos() {
        return todoRepository.findAll();
    }

    public Optional<Todo> getTodoById(Long id) {
        return todoRepository.findById(id);
    }

    public List<Todo> getTodoBy(String category) {
        return todoRepository.findByCategory(category);
    }

    public Todo createTodo(Todo todo) {
        if (todo.getCreatedAt() == null) {
            todo.setCreatedAt(LocalDateTime.now());
        }
        todo.setUpdatedAt(LocalDateTime.now());
        return todoRepository.save(todo);
    }

    public Todo updateTodo(Todo todo) {
        todo.setUpdatedAt(LocalDateTime.now());
        return todoRepository.save(todo);
    }

    public Optional<Todo> updateTodo(Long id, Todo todoDetails) {
        return todoRepository.findById(id).map(todo -> {
            todo.setTitle(todoDetails.getTitle());
            todo.setDescription(todoDetails.getDescription());
            todo.setCategory(todoDetails.getCategory());
            todo.setCompleted(todoDetails.isCompleted());
            todo.setUpdatedAt(LocalDateTime.now());
            return todoRepository.save(todo);
        });
    }

    public boolean deleteTodo(Long id) {
        return todoRepository.findById(id).map(todo -> {
            todoRepository.delete(todo);
            return true;
        }).orElse(false);
    }

    public boolean deleteAllTodos() {
        todoRepository.deleteAll();
        return true;
    }

    public List<Todo> getTodosByCompleted(boolean completed) {
        return todoRepository.findByCompleted(completed);
    }

    public List<Todo> getTodosByCategoryAndCompleted(String category, boolean completed) {
        return todoRepository.findByCategoryAndCompleted(category, completed);
    }

    public List<Todo> searchTodosByTitle(String title) {
        return todoRepository.findByTitleContainingIgnoreCase(title);
    }

    public List<Todo> searchTodosByDescription(String description) {
        return todoRepository.findByDescriptionContainingIgnoreCase(description);
    }

    public boolean isValidCategory(String category) {
        if (category == null) return false;
        return List.of("WORK", "PERSONAL", "SHOPPING", "HEALTH", "STUDY", "HOME", "OTHER")
                .contains(category.toUpperCase());
    }
}
