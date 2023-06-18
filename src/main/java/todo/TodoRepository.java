package todo;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.repository.CrudRepository;

public interface TodoRepository extends CrudRepository<TodoEntity, UUID> {
    Iterable<TodoEntity> findByOwner(String owner);

    Optional<TodoEntity> findByIdAndOwner(UUID id, String owner);

    void deleteByOwner(String owner);
}
