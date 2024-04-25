package pawlin.userapi.service.implementation;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.hateoas.CollectionModel;
import pawlin.userapi.dto.UserDto;
import pawlin.userapi.dto.UserRequestDto;
import pawlin.userapi.dto.mapper.UserDtoModelAssembler;
import pawlin.userapi.exception.EntityNotFoundException;
import pawlin.userapi.exception.RequestParameterNotValidException;
import pawlin.userapi.model.User;
import pawlin.userapi.repository.UserRepository;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private UserDtoModelAssembler userDtoModelAssembler;
    @Mock
    private Validator validator;
    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void shouldFindAll() {
        // given
        User user = new User(1L, "email", "firstName", "lastName", LocalDate.now(), "address", "phoneNumber");
        UserDto userDto = new UserDto(1L, "email", "firstName", "lastName", LocalDate.now(), "address", "phoneNumber");
        given(userRepository.findAll()).willReturn(List.of(user));
        given(userDtoModelAssembler.toCollectionModel(List.of(user))).willReturn(CollectionModel.of(List.of(userDto)));

        // when
        CollectionModel<UserDto> users = userService.findAll(null, null);

        // then
        assertEquals(1, users.getContent().size());
        assertTrue(users.getContent().contains(userDto));
    }

    @Test
    void shouldFindById() {
        // given
        User user = new User(1L, "email", "firstName", "lastName", LocalDate.now(), "address", "phoneNumber");
        UserDto userDto = new UserDto(1L, "email", "firstName", "lastName", LocalDate.now(), "address", "phoneNumber");

        // when
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userDtoModelAssembler.toModel(user)).thenReturn(userDto);
        UserDto foundUser = userService.findById(1L);

        // then
        assertEquals(userDto, foundUser);
    }

    @Test
    void shouldNotFindById() {
        // given

        // when
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        // then
        assertThrows(EntityNotFoundException.class, () -> userService.findById(1L));
    }

    @Test
    void shouldFindByBirthDateBetween() {
        // given
        LocalDate startDate = LocalDate.now().minusDays(2);
        LocalDate endDate = LocalDate.now().minusDays(1);

        User user1 = new User(1L, "email", "firstName", "lastName", startDate, "address", "phoneNumber");
        User user2 = new User(2L, "email", "firstName", "lastName", endDate, "address", "phoneNumber");
        UserDto userDto1 = new UserDto(1L, "email", "firstName", "lastName", startDate, "address", "phoneNumber");
        UserDto userDto2 = new UserDto(2L, "email", "firstName", "lastName", endDate, "address", "phoneNumber");

        // when
        when(userRepository.findByBirthDateBetween(startDate, endDate)).thenReturn(List.of(user1, user2));
        when(userDtoModelAssembler.toCollectionModel(List.of(user1, user2))).thenReturn(CollectionModel.of(List.of(userDto1, userDto2)));
        CollectionModel<UserDto> users = userService.findAll(startDate, endDate);

        // then
        assertEquals(2, users.getContent().size());
        assertTrue(users.getContent().contains(userDto1));
        assertTrue(users.getContent().contains(userDto2));
    }

    @Test
    void shouldNotFindByBirthDateBetween() {
        // given
        LocalDate startDate = LocalDate.now().minusDays(2);
        LocalDate endDate = LocalDate.now().minusDays(1);

        // when
        when(userRepository.findByBirthDateBetween(startDate, endDate)).thenReturn(List.of());
        when(userDtoModelAssembler.toCollectionModel(List.of())).thenReturn(CollectionModel.of(List.of()));
        CollectionModel<UserDto> users = userService.findAll(startDate, endDate);

        // then
        assertTrue(users.getContent().isEmpty());
    }

    @Test
    void shouldNotFindByBirthDateBetweenWhenInvalidRange() {
        // when
        LocalDate startDate = LocalDate.now().minusDays(1);
        LocalDate endDate = LocalDate.now().minusDays(2);

        // then
        assertThrows(RequestParameterNotValidException.class, () -> userService.findAll(startDate, endDate));
    }

    @Test
    void shouldFindByBirthDateBetweenWhenEndDateUnspecified() {
        // given
        LocalDate startDate = LocalDate.now().minusDays(2);

        // when
        when(userRepository.findByBirthDateAfter(startDate)).thenReturn(List.of());
        when(userDtoModelAssembler.toCollectionModel(List.of())).thenReturn(CollectionModel.of(List.of()));
        CollectionModel<UserDto> users = userService.findAll(startDate, null);

        // then
        assertTrue(users.getContent().isEmpty());
    }

    @Test
    void shouldFindByBirthDateBetweenWhenStartDateUnspecified() {
        // given
        LocalDate endDate = LocalDate.now().minusDays(2);

        // when
        when(userRepository.findByBirthDateBefore(endDate)).thenReturn(List.of());
        when(userDtoModelAssembler.toCollectionModel(List.of())).thenReturn(CollectionModel.of(List.of()));
        CollectionModel<UserDto> users = userService.findAll(null, endDate);

        // then
        assertTrue(users.getContent().isEmpty());
    }

    @Test
    void shouldCreate() {
        // given
        LocalDate birthDate = LocalDate.now().minusYears(5);
        User user = new User(null, "email", "firstName", "lastName", birthDate, "address", "phoneNumber");
        UserRequestDto userRequestDto = new UserRequestDto("email", "firstName", "lastName", birthDate, "address", "phoneNumber");

        // when
        when(userRepository.create(user)).thenReturn(15L);
        when(validator.validate(userRequestDto)).thenReturn(Set.of());
        Long id = userService.create(userRequestDto);

        // then
        assertEquals(15L, id);
    }

    @Test
    void shouldNotCreateWhenValidationFails() {
        // given
        UserRequestDto userRequestDto = new UserRequestDto("email", "firstName", "lastName", LocalDate.now(), "address", "phoneNumber");
        @SuppressWarnings("unchecked")
        ConstraintViolation<UserRequestDto> constraintViolation = mock(ConstraintViolation.class);

        // when
        when(validator.validate(userRequestDto)).thenReturn(Set.of(constraintViolation));

        // then
        assertThrows(ConstraintViolationException.class, () -> userService.create(userRequestDto));
    }

    @Test
    void shouldUpdate() {
        // given
        User user = new User(1L, "email", "firstName", "lastName", LocalDate.now(), "address", "phoneNumber");
        UserRequestDto userRequestDto = new UserRequestDto("email", "firstName", "lastName", LocalDate.now(), "address", "phoneNumber");
        UserDto userDto = new UserDto(1L, "email", "firstName", "lastName", LocalDate.now(), "address", "phoneNumber");

        // when
        when(userRepository.update(user)).thenReturn(user);
        when(validator.validate(userRequestDto)).thenReturn(Set.of());
        when(userDtoModelAssembler.toModel(user)).thenReturn(userDto);
        UserDto updatedUser = userService.update(1L, userRequestDto);

        // then
        assertEquals(userDto, updatedUser);
    }

    @Test
    void shouldNotUpdateWhenValidationFails() {
        // given
        UserRequestDto userRequestDto = new UserRequestDto("email", "firstName", "lastName", LocalDate.now(), "address", "phoneNumber");
        @SuppressWarnings("unchecked")
        ConstraintViolation<UserRequestDto> constraintViolation = mock(ConstraintViolation.class);

        // when
        when(validator.validate(userRequestDto)).thenReturn(Set.of(constraintViolation));

        // then
        assertThrows(ConstraintViolationException.class, () -> userService.update(1L, userRequestDto));
    }

    @Test
    void shouldUpdatePartially() {
        // given
        User user = new User(1L, "email", "firstName", "lastName", LocalDate.now(), "address", "phoneNumber");
        UserRequestDto userRequestDto = new UserRequestDto(null, "newFirstName", null, null, null, null);
        UserDto userDto = new UserDto(1L, "email", "firstName", "lastName", LocalDate.now(), "address", "phoneNumber");

        // when
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(validator.validateProperty(userRequestDto, "firstName")).thenReturn(Set.of());
        when(userRepository.update(user)).thenReturn(user);
        when(userDtoModelAssembler.toModel(user)).thenReturn(userDto);

        UserDto updatedUser = userService.partialUpdate(1L, userRequestDto);

        // then
        assertEquals(userDto, updatedUser);
    }

    @Test
    void shouldNotUpdatePartiallyWhenValidationFails() {
        // given
        User user = new User(1L, "email", "firstName", "lastName", LocalDate.now(), "address", "phoneNumber");
        UserRequestDto userRequestDto = new UserRequestDto(null, "", null, null, null, null);
        @SuppressWarnings("unchecked")
        ConstraintViolation<UserRequestDto> constraintViolation = mock(ConstraintViolation.class);

        // when
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(validator.validateProperty(userRequestDto, "firstName")).thenReturn(Set.of(constraintViolation));

        // then
        assertThrows(ConstraintViolationException.class, () -> userService.partialUpdate(1L, userRequestDto));
    }

    @Test
    void shouldDelete() {
        // when
        when(userRepository.existsById(1L)).thenReturn(true);
        userService.delete(1L);

        // then
        verify(userRepository).delete(1L);
    }

    @Test
    void shouldNotDelete() {
        // when
        when(userRepository.existsById(1L)).thenReturn(false);

        // then
        assertThrows(EntityNotFoundException.class, () -> userService.delete(1L));
    }
}