package pawlin.userapi.model;

import lombok.*;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class User {
    @With
    @Setter(AccessLevel.NONE)
    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private LocalDate birthDate;
    private String address;
    private String phoneNumber;
}
