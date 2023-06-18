package todo;

import java.net.URI;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import todo.api.TodoApi;
import todo.model.CreateTodoItem;
import todo.model.TodoItem;
import todo.model.TodoStatus;

@RestController
@AllArgsConstructor
@Slf4j
@RequestMapping("/v1")
public class TodoController implements TodoApi {

    private final TodoService service;

    @Override
    public ResponseEntity<List<TodoItem>> allTodoItems() {
        log.info("get all items");
        return ResponseEntity.ok(service.allTodoItems());
    }

    @Override
    public ResponseEntity<Void> changeTodoItem(UUID id, TodoStatus todoStatus) {
        log.info("change status of {} to {}", id, todoStatus);
        service.updateStatus(id, todoStatus);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<TodoItem> createTodoItem(CreateTodoItem createTodoItem) {
        log.info("create item: {}", createTodoItem);
        final TodoItem todoItem = service.save(createTodoItem);
        return ResponseEntity.created(URI.create("/" + todoItem.getId())).body(todoItem);
    }

    @Override
    public ResponseEntity<Void> deleteTodoItem(UUID id) {
        log.info("delete item {}", id);
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<TodoItem> getTodoItem(UUID id) {
        log.info("get todo item {}", id);
        return ResponseEntity.ok(service.findById(id));
    }

    @Override
    public ResponseEntity<Void> deleteAllTodoItems() {
        log.info("delete all items");
        service.deleteAll();
        return ResponseEntity.noContent().build();
    }
}
