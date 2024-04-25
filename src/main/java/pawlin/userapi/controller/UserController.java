package pawlin.userapi.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pawlin.userapi.dto.UserDto;
import pawlin.userapi.dto.UserRequestDto;
import pawlin.userapi.service.UserService;

import java.time.LocalDate;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    @GetMapping
    public ResponseEntity<CollectionModel<UserDto>> getAll(@RequestParam(required = false) LocalDate startDate, @RequestParam(required = false) LocalDate endDate) {
        return ResponseEntity.ok(userService.findAll(startDate, endDate));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getOne(@PathVariable Long id) {
        return ResponseEntity.ok(userService.findById(id));
    }

    @PostMapping
    public ResponseEntity<Void> create(@RequestBody UserRequestDto userRequestDto) {
        Long id = userService.create(userRequestDto);
        return ResponseEntity.created(linkTo(methodOn(UserController.class).getOne(id)).toUri()).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDto> update(@PathVariable Long id, @RequestBody UserRequestDto userRequestDto) {
        return ResponseEntity.ok(userService.update(id, userRequestDto));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<UserDto> partialUpdate(@PathVariable Long id, @RequestBody UserRequestDto userRequestDto) {
        return ResponseEntity.ok(userService.partialUpdate(id, userRequestDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
