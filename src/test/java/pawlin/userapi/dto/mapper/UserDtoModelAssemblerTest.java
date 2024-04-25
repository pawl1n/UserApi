package pawlin.userapi.dto.mapper;

import org.junit.jupiter.api.Test;
import org.springframework.hateoas.CollectionModel;
import pawlin.userapi.dto.UserDto;
import pawlin.userapi.model.User;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserDtoModelAssemblerTest {
    private final UserDtoModelAssembler userDtoModelAssembler = new UserDtoModelAssembler();

    @Test
    void shouldMapToModel() {
        // given
        User user = new User(1L, "email", "firstName", "lastName", LocalDate.now(), "address", "phoneNumber");
        UserDto userDto = new UserDto(1L, "email", "firstName", "lastName", LocalDate.now(), "address", "phoneNumber");

        // when
        UserDto result = userDtoModelAssembler.toModel(user);

        // then
        assertEquals(userDto, result);
    }

    @Test
    void shouldMapToCollectionModel() {
        // given
        User user = new User(1L, "email", "firstName", "lastName", LocalDate.now(), "address", "phoneNumber");
        UserDto userDto = new UserDto(1L, "email", "firstName", "lastName", LocalDate.now(), "address", "phoneNumber");

        // when
        CollectionModel<UserDto> result = userDtoModelAssembler.toCollectionModel(List.of(user));

        // then
        assertEquals(1, result.getContent().size());
        assertTrue(result.getContent().contains(userDto));
    }
}