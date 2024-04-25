package pawlin.userapi.service;

import org.springframework.hateoas.CollectionModel;
import pawlin.userapi.dto.UserDto;
import pawlin.userapi.dto.UserRequestDto;

import java.time.LocalDate;

public interface UserService {
    CollectionModel<UserDto> findAll(LocalDate startDate, LocalDate endDate);

    UserDto findById(Long id);
    Long create(UserRequestDto userRequestDto);

    UserDto update(Long id, UserRequestDto userRequestDto);

    UserDto partialUpdate(Long id, UserRequestDto userRequestDto);

    void delete(Long id);
}
