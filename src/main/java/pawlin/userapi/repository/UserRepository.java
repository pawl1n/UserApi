package pawlin.userapi.repository;

import pawlin.userapi.model.User;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface UserRepository {
    List<User> findAll();

    Optional<User> findById(Long id);

    List<User> findByBirthDateBetween(LocalDate startDate, LocalDate endDate);

    List<User> findByBirthDateAfter(LocalDate date);

    List<User> findByBirthDateBefore(LocalDate date);

    Long create(User user);

    User update(User user);

    void delete(Long id);

    boolean existsById(Long id);
}
