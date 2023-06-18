package todo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import todo.model.TodoItem;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "todos")
public class TodoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false, columnDefinition = "UUID default gen_random_uuid()")
    private final UUID id = UUID.randomUUID();

    private String owner;

    private String text;

    @Setter
    private Boolean done;

    public TodoEntity(String owner, String text, Boolean done) {
        this.owner = owner;
        this.text = text;
        this.done = done;
    }

    public TodoItem toTodoItem() {
        return new TodoItem().id(id).text(text).done(done);
    }
}
