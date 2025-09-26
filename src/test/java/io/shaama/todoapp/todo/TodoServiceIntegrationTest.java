package io.shaama.todoapp.todo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = io.shaama.todoapp.TodoappApplication.class)
@Transactional
@ActiveProfiles("test")
public class TodoServiceIntegrationTest {

    @Autowired
    private TodoService todoService;

    @Test
    void createTodo_ShouldSaveTodoAndSetTimestamps() {
        // Given
        Todo todo = Todo.builder()
                .title("Test Todo")
                .description("Test Description")
                .completed(false)
                .build();

        // When
        Todo savedTodo = todoService.createTodo(todo);

        // Then
        assertThat(savedTodo.getId()).isNotNull();
        assertThat(savedTodo.getTitle()).isEqualTo("Test Todo");
        assertThat(savedTodo.getDescription()).isEqualTo("Test Description");
        assertThat(savedTodo.isCompleted()).isFalse();
        assertThat(savedTodo.getCreatedAt()).isNotNull();
        assertThat(savedTodo.getUpdatedAt()).isNotNull();
    }

    @Test
    void getAllTodos_ShouldReturnAllTodos() {
        // Given
        Todo todo1 = Todo.builder().title("Todo 1").build();
        Todo todo2 = Todo.builder().title("Todo 2").build();

        todoService.createTodo(todo1);
        todoService.createTodo(todo2);

        // When
        List<Todo> todos = todoService.getAllTodos();

        // Then
        assertThat(todos).hasSize(2);
        assertThat(todos).extracting("title")
                        .containsExactlyInAnyOrder("Todo 1", "Todo 2");
    }

    @Test
    void getTodoById_WithExistingId_ShouldReturnTodo() {
        // Given
        Todo todo = Todo.builder().title("Test Todo").build();
        Todo savedTodo = todoService.createTodo(todo);

        // When
        Optional<Todo> foundTodo = todoService.getTodoById(savedTodo.getId());

        // Then
        assertThat(foundTodo).isPresent();
        assertThat(foundTodo.get().getTitle()).isEqualTo("Test Todo");
    }

    @Test
    void getTodoById_WithNonExistingId_ShouldReturnEmpty() {
        // When
        Optional<Todo> foundTodo = todoService.getTodoById(999L);

        // Then
        assertThat(foundTodo).isEmpty();
    }

    @Test
    void updateTodo_WithExistingId_ShouldUpdateTodo() {
        // Given
        Todo todo = Todo.builder().title("Original Title").build();
        Todo savedTodo = todoService.createTodo(todo);

        Todo updateDetails = Todo.builder()
                .title("Updated Title")
                .completed(true)
                .build();

        // When
        Optional<Todo> updatedTodo = todoService.updateTodo(savedTodo.getId(), updateDetails);

        // Then
        assertThat(updatedTodo).isPresent();
        assertThat(updatedTodo.get().getTitle()).isEqualTo("Updated Title");
        assertThat(updatedTodo.get().isCompleted()).isTrue();
        assertThat(updatedTodo.get().getUpdatedAt())
                .isAfterOrEqualTo(updatedTodo.get().getCreatedAt());
    }

    @Test
    void deleteTodo_WithExistingId_ShouldDeleteTodoAndReturnTrue() {
        // Given
        Todo todo = Todo.builder().title("Todo to delete").build();
        Todo savedTodo = todoService.createTodo(todo);

        // When
        boolean deleted = todoService.deleteTodo(savedTodo.getId());

        // Then
        assertThat(deleted).isTrue();
        assertThat(todoService.getTodoById(savedTodo.getId())).isEmpty();
    }

    @Test
    void deleteTodo_WithNonExistingId_ShouldReturnFalse() {
        // When
        boolean deleted = todoService.deleteTodo(999L);

        // Then
        assertThat(deleted).isFalse();
    }
}
