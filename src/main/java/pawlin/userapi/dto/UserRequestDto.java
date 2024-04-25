package pawlin.userapi.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import pawlin.userapi.dto.validation.LegalDate;

import java.time.LocalDate;

public record UserRequestDto(@Email String email, @NotBlank String firstName, @NotBlank String lastName,
                             @LegalDate LocalDate birthDate, String address, String phoneNumber) {
}
