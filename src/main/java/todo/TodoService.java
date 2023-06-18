package todo;

import static java.util.stream.StreamSupport.stream;

import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import todo.model.CreateTodoItem;
import todo.model.TodoItem;
import todo.model.TodoStatus;

@Service
@Slf4j
@AllArgsConstructor
public class TodoService {
    private final TodoRepository db;

    private static TodoEntity toEntity(CreateTodoItem todoItem, String owner) {
        return new TodoEntity(owner, todoItem.getText(), false);
    }

    public List<TodoItem> allTodoItems() {
        var owner = getCurrentUserOrFail();
        return stream(db.findByOwner(owner).spliterator(), false).map(TodoEntity::toTodoItem).toList();
    }

    public TodoItem save(final CreateTodoItem dto) {
        var owner = getCurrentUserOrFail();
        return db.save(toEntity(dto, owner)).toTodoItem();
    }

    public TodoItem findById(final UUID id) {
        var owner = getCurrentUserOrFail();
        return db.findByIdAndOwner(id, owner).map(TodoEntity::toTodoItem).orElseThrow(NotFoundException::new);
    }

    public void updateStatus(final UUID id, final TodoStatus status) {
        var owner = getCurrentUserOrFail();
        final TodoEntity entity = db.findByIdAndOwner(id, owner).orElseThrow(NotFoundException::new);
        entity.setDone(status.getDone());
        db.save(entity);
    }

    public void delete(final UUID id) {
        var owner = getCurrentUserOrFail();
        final TodoEntity entity = db.findByIdAndOwner(id, owner).orElseThrow(NotFoundException::new);
        db.delete(entity);
    }

    @Transactional
    public void deleteAll() {
        var owner = getCurrentUserOrFail();
        db.deleteByOwner(owner);
    }

    private String getCurrentUserOrFail() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new NotAuthenticatedException();
        }
        final String username = authentication.getName();
        log.debug("found user {}", username);
        return username;
    }
}
