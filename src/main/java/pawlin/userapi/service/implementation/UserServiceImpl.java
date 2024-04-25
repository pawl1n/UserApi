package pawlin.userapi.service.implementation;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.stereotype.Service;
import pawlin.userapi.dto.UserDto;
import pawlin.userapi.dto.UserRequestDto;
import pawlin.userapi.dto.mapper.UserDtoModelAssembler;
import pawlin.userapi.exception.EntityNotFoundException;
import pawlin.userapi.exception.RequestParameterNotValidException;
import pawlin.userapi.model.User;
import pawlin.userapi.repository.UserRepository;
import pawlin.userapi.service.UserService;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserDtoModelAssembler userDtoModelAssembler;
    private final UserRepository userRepository;
    private final Validator validator;

    @Override
    public CollectionModel<UserDto> findAll(LocalDate startDate, LocalDate endDate) {
        if (startDate == null && endDate == null) {
            return userDtoModelAssembler.toCollectionModel(userRepository.findAll());
        }

        List<User> users;

        if (startDate != null && endDate == null) {
            users = userRepository.findByBirthDateAfter(startDate);
        } else if (startDate == null) {
            users = userRepository.findByBirthDateBefore(endDate);
        }
        else if (startDate.isAfter(endDate)) {
            throw new RequestParameterNotValidException("Invalid date range");
        }
        else {
            users = userRepository.findByBirthDateBetween(startDate, endDate);
        }

        return userDtoModelAssembler.toCollectionModel(users);
    }

    @Override
    public UserDto findById(Long id) {
        return userDtoModelAssembler.toModel(userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(id)));
    }

    @Override
    public Long create(UserRequestDto userRequestDto) {
        Set<ConstraintViolation<UserRequestDto>> violations = validator.validate(userRequestDto);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }

        User user = new User(null, userRequestDto.email(), userRequestDto.firstName(), userRequestDto.lastName(), userRequestDto.birthDate(), userRequestDto.address(), userRequestDto.phoneNumber());
        return userRepository.create(user);
    }

    @Override
    public UserDto update(Long id, UserRequestDto userRequestDto) {
        Set<ConstraintViolation<UserRequestDto>> violations = validator.validate(userRequestDto);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }

        User user = new User(id, userRequestDto.email(), userRequestDto.firstName(), userRequestDto.lastName(), userRequestDto.birthDate(), userRequestDto.address(), userRequestDto.phoneNumber());
        return userDtoModelAssembler.toModel(userRepository.update(user));
    }

    @Override
    public UserDto partialUpdate(Long id, UserRequestDto userRequestDto) {
        User user = userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(id));
        Set<ConstraintViolation<UserRequestDto>> violations = new HashSet<>();

        if (userRequestDto.email() != null) {
            violations.addAll(validator.validateProperty(userRequestDto, "email"));
            user.setEmail(userRequestDto.email());
        }
        if (userRequestDto.firstName() != null) {
            violations.addAll(validator.validateProperty(userRequestDto, "firstName"));
            user.setFirstName(userRequestDto.firstName());
        }
        if (userRequestDto.lastName() != null) {
            violations.addAll(validator.validateProperty(userRequestDto, "lastName"));
            user.setLastName(userRequestDto.lastName());
        }
        if (userRequestDto.birthDate() != null) {
            violations.addAll(validator.validateProperty(userRequestDto, "birthDate"));
            user.setBirthDate(userRequestDto.birthDate());
        }
        if (userRequestDto.address() != null) {
            validator.validateProperty(userRequestDto, "address");
            user.setAddress(userRequestDto.address());
        }
        if (userRequestDto.phoneNumber() != null) {
            validator.validateProperty(userRequestDto, "phoneNumber");
            user.setPhoneNumber(userRequestDto.phoneNumber());
        }

        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }

        return userDtoModelAssembler.toModel(userRepository.update(user));
    }

    @Override
    public void delete(Long id) {
        if (!userRepository.existsById(id)) {
            throw new EntityNotFoundException(id);
        }

        userRepository.delete(id);
    }
}
