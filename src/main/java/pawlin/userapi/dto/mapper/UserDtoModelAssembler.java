package pawlin.userapi.dto.mapper;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import pawlin.userapi.controller.UserController;
import pawlin.userapi.dto.UserDto;
import pawlin.userapi.model.User;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class UserDtoModelAssembler implements RepresentationModelAssembler<User, UserDto> {
    @Override
    @NonNull
    public UserDto toModel(@NonNull User entity) {
        UserDto dto = new UserDto(entity.getId(), entity.getEmail(), entity.getFirstName(), entity.getLastName(), entity.getBirthDate(), entity.getAddress(), entity.getPhoneNumber());
        dto.add(linkTo(methodOn(UserController.class).getOne(entity.getId())).withSelfRel());

        return dto;
    }

    @Override
    @NonNull
    public CollectionModel<UserDto> toCollectionModel(@NonNull Iterable<? extends User> entities) {
        return RepresentationModelAssembler.super.toCollectionModel(entities).add(linkTo(UserController.class).withSelfRel());
    }
}
