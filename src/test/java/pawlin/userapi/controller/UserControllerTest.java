package pawlin.userapi.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import pawlin.userapi.dto.UserDto;
import pawlin.userapi.dto.UserRequestDto;
import pawlin.userapi.service.UserService;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {
    @Mock
    private UserService userService;
    @InjectMocks
    private UserController userController;

    @Test
    void shouldGetAll() {
        // given
        given(userService.findAll(null, null)).willReturn(CollectionModel.of(List.of(new UserDto(1L, "email", "firstName", "lastName", LocalDate.now(), "address", "phoneNumber"))));

        // when
        ResponseEntity<CollectionModel<UserDto>> response = userController.getAll(null, null);

        // then
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().getContent().size());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void shouldGetAllWithDateRange() {
        // given
        LocalDate startDate = LocalDate.now().minusYears(1);
        LocalDate endDate = LocalDate.now();
        given(userService.findAll(startDate, endDate)).willReturn(CollectionModel.of(List.of(new UserDto(1L, "email", "firstName", "lastName", LocalDate.now(), "address", "phoneNumber"))));

        // when
        ResponseEntity<CollectionModel<UserDto>> response = userController.getAll(startDate, endDate);

        // then
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().getContent().size());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void shouldGetOne() {
        // given
        given(userService.findById(1L)).willReturn(new UserDto(1L, "email", "firstName", "lastName", LocalDate.now(), "address", "phoneNumber"));

        // when
        ResponseEntity<UserDto> response = userController.getOne(1L);

        // then
        assertNotNull(response.getBody());
        assertEquals(1L, response.getBody().getId());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void shouldCreate() {
        // given
        UserRequestDto userRequestDto = new UserRequestDto("email", "firstName", "lastName", LocalDate.now(), "address", "phoneNumber");
        given(userService.create(userRequestDto)).willReturn(1L);

        // when
        ResponseEntity<Void> response = userController.create(userRequestDto);

        // then
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(linkTo(methodOn(UserController.class).getOne(1L)).toUri(), response.getHeaders().getLocation());
    }

    @Test
    void shouldUpdate() {
        // given
        UserRequestDto userRequestDto = new UserRequestDto("email", "firstName", "lastName", LocalDate.now(), "address", "phoneNumber");
        UserDto userDto = new UserDto(1L, "email", "firstName", "lastName", LocalDate.now(), "address", "phoneNumber");
        given(userService.update(1L, userRequestDto)).willReturn(userDto);

        // when
        ResponseEntity<UserDto> response = userController.update(1L, userRequestDto);

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(userDto, response.getBody());
    }

    @Test
    void shouldUpdatePartially() {
        // given
        UserRequestDto userRequestDto = new UserRequestDto(null, "newFirstName", null, null, null, null);
        UserDto userDto = new UserDto(1L, "email", "newFirstName", "lastName", LocalDate.now(), "address", "phoneNumber");
        given(userService.partialUpdate(1L, userRequestDto)).willReturn(userDto);

        // when
        ResponseEntity<UserDto> response = userController.partialUpdate(1L, userRequestDto);

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(userDto, response.getBody());
    }

    @Test
    void shouldDelete() {
        // given

        // when
        ResponseEntity<Void> response = userController.delete(1L);

        // then
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(userService).delete(1L);
    }
}