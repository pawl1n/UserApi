package pawlin.userapi.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import java.time.LocalDate;

@Relation(value = "user", collectionRelation = "users")
@JsonInclude(JsonInclude.Include.NON_NULL)
@EqualsAndHashCode(callSuper = false)
@Data
public class UserDto extends RepresentationModel<UserDto> {
    private final Long id;
    private final String email;
    private final String firstName;
    private final String lastName;
    private final LocalDate birthDate;
    private final String address;
    private final String phoneNumber;
}
