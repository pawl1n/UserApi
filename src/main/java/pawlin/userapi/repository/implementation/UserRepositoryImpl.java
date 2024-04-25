package pawlin.userapi.repository.implementation;

import org.springframework.stereotype.Repository;
import pawlin.userapi.model.User;
import pawlin.userapi.repository.UserRepository;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class UserRepositoryImpl implements UserRepository {
    private final Map<Long, User> users;

    public UserRepositoryImpl(Map<Long, User> users) {
        this.users = new HashMap<>(users);
    }

    @Override
    public List<User> findAll() {
        return users.values().stream().toList();
    }

    @Override
    public Optional<User> findById(Long id) {
        return Optional.ofNullable(users.get(id));
    }

    @Override
    public List<User> findByBirthDateBetween(LocalDate startDate, LocalDate endDate) {
        return users.values().stream().filter(user -> !user.getBirthDate().isBefore(startDate) && !user.getBirthDate().isAfter(endDate)).toList();
    }

    @Override
    public List<User> findByBirthDateAfter(LocalDate date) {
        return users.values().stream().filter(user -> !user.getBirthDate().isBefore(date)).toList();
    }

    @Override
    public List<User> findByBirthDateBefore(LocalDate date) {
        return users.values().stream().filter(user -> !user.getBirthDate().isAfter(date)).toList();
    }

    @Override
    public Long create(User user) {
        Long id = users.keySet().stream().max(Long::compareTo).orElse(0L) + 1;
        users.put(id, user.withId(id));

        return id;
    }

    @Override
    public User update(User user) {
        users.put(user.getId(), user);
        return users.get(user.getId());
    }

    @Override
    public void delete(Long id) {
        users.remove(id);
    }

    @Override
    public boolean existsById(Long id) {
        return users.containsKey(id);
    }
}
