package pawlin.userapi.repository.implementation;

import org.junit.jupiter.api.Test;
import pawlin.userapi.model.User;
import pawlin.userapi.repository.UserRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class UserRepositoryImplTest {
    @Test
    void shouldFindAll() {
        // given
        User user1 = new User(1L, "email", "firstName", "lastName", LocalDate.now(), "address", "phoneNumber");
        User user2 = new User(2L, "email", "firstName", "lastName", LocalDate.now(), "address", "phoneNumber");

        UserRepository userRepository = new UserRepositoryImpl(Map.of(1L, user1, 2L, user2));

        // when
        List<User> users = userRepository.findAll();

        // then
        assertEquals(2, users.size());
        assertTrue(users.contains(user1));
        assertTrue(users.contains(user2));
    }

    @Test
    void shouldFindById() {
        // given
        User user = new User(1L, "email", "firstName", "lastName", LocalDate.now(), "address", "phoneNumber");
        UserRepository userRepository = new UserRepositoryImpl(Map.of(1L, user));

        // when
        Optional<User> foundUser = userRepository.findById(1L);

        // then
        assertTrue(foundUser.isPresent());
        assertEquals(user, foundUser.get());
    }

    @Test
    void shouldNotFindById() {
        // given
        User user = new User(1L, "email", "firstName", "lastName", LocalDate.now(), "address", "phoneNumber");
        UserRepository userRepository = new UserRepositoryImpl(Map.of(1L, user));

        // when
        Optional<User> foundUser = userRepository.findById(2L);

        // then
        assertTrue(foundUser.isEmpty());
    }

    @Test
    void shouldFindByBirthDateBetween() {
        // given
        LocalDate now = LocalDate.now();
        User user1 = new User(1L, "email", "firstName", "lastName", now.minusDays(1), "address", "phoneNumber");
        User user2 = new User(2L, "email", "firstName", "lastName", now.minusDays(2), "address", "phoneNumber");

        UserRepository userRepository = new UserRepositoryImpl(Map.of(1L, user1, 2L, user2));

        // when
        List<User> users = userRepository.findByBirthDateBetween(now.minusDays(2), now);

        // then
        assertEquals(2, users.size());
        assertTrue(users.contains(user1));
        assertTrue(users.contains(user2));
    }

    @Test
    void shouldFindByBirthDateAfter() {
        // given
        LocalDate now = LocalDate.now();
        User user1 = new User(1L, "email", "firstName", "lastName", now.minusDays(1), "address", "phoneNumber");
        User user2 = new User(2L, "email", "firstName", "lastName", now.minusDays(2), "address", "phoneNumber");

        UserRepository userRepository = new UserRepositoryImpl(Map.of(1L, user1, 2L, user2));

        // when
        List<User> users = userRepository.findByBirthDateAfter(now.minusDays(2));

        // then
        assertEquals(2, users.size());
        assertTrue(users.containsAll(List.of(user1, user2)));
    }

    @Test
    void shouldFindByBirthDateBefore() {
        // given
        LocalDate now = LocalDate.now();
        User user1 = new User(1L, "email", "firstName", "lastName", now.minusDays(1), "address", "phoneNumber");
        User user2 = new User(2L, "email", "firstName", "lastName", now.minusDays(2), "address", "phoneNumber");

        UserRepository userRepository = new UserRepositoryImpl(Map.of(1L, user1, 2L, user2));

        // when
        List<User> users = userRepository.findByBirthDateBefore(now.minusDays(2));

        // then
        assertEquals(1, users.size());
        assertFalse(users.contains(user1));
        assertTrue(users.contains(user2));
    }

    @Test
    void shouldCreateMultipleUsersWithDifferentIds() {
        // given
        User user1 = new User(1L, "email", "firstName", "lastName", LocalDate.now(), "address", "phoneNumber");
        User user2 = new User(2L, "email", "firstName", "lastName", LocalDate.now(), "address", "phoneNumber");
        UserRepository userRepository = new UserRepositoryImpl(Map.of());

        // when
        Long id1 = userRepository.create(user1);
        Long id2 = userRepository.create(user2);

        // then
        assertEquals(1L, id1);
        assertEquals(2L, id2);
    }

    @Test
    void shouldCreateUsersAndSaveThem() {
        // given
        User user1 = new User(1L, "email", "firstName", "lastName", LocalDate.now(), "address", "phoneNumber");
        User user2 = new User(2L, "email", "firstName", "lastName", LocalDate.now(), "address", "phoneNumber");
        UserRepository userRepository = new UserRepositoryImpl(Map.of());

        // when
        Long id1 = userRepository.create(user1);
        Long id2 = userRepository.create(user2);

        // then
        Optional<User> result1 = userRepository.findById(id1);
        assertTrue(result1.isPresent());
        assertEquals(user1, result1.get());

        Optional<User> result2 = userRepository.findById(id2);
        assertTrue(result2.isPresent());
        assertEquals(user2, result2.get());
    }

    @Test
    void update() {
        // given
        User user = new User(1L, "email", "firstName", "lastName", LocalDate.now(), "address", "phoneNumber");
        UserRepository userRepository = new UserRepositoryImpl(Map.of(1L, user));

        // when
        User updatedUser = new User(1L, "newEmail", "newFirstName", "newLastName", LocalDate.now(), "newAddress", "newPhoneNumber");
        userRepository.update(updatedUser);

        // then
        Optional<User> result = userRepository.findById(1L);
        assertTrue(result.isPresent());
        assertEquals(updatedUser, result.get());
    }

    @Test
    void delete() {
        // given
        User user = new User(1L, "email", "firstName", "lastName", LocalDate.now(), "address", "phoneNumber");
        UserRepository userRepository = new UserRepositoryImpl(Map.of(1L, user));

        // when
        userRepository.delete(1L);

        // then
        Optional<User> result = userRepository.findById(1L);
        assertTrue(result.isEmpty());
    }

    @Test
    void existsById() {
        // given
        User user = new User(1L, "email", "firstName", "lastName", LocalDate.now(), "address", "phoneNumber");

        // when
        UserRepository userRepository = new UserRepositoryImpl(Map.of(1L, user));

        // then
        assertTrue(userRepository.existsById(1L));
    }
}