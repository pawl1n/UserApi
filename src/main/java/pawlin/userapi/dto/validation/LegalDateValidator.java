package pawlin.userapi.dto.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import pawlin.userapi.configuration.UserProperties;

import java.time.LocalDate;

@RequiredArgsConstructor
public class LegalDateValidator implements ConstraintValidator<LegalDate, LocalDate> {
    private final UserProperties userProperties;

    @Override
    public boolean isValid(LocalDate date, ConstraintValidatorContext constraintValidatorContext) {
        return date != null && date.isBefore(LocalDate.now()) && !date.isAfter(LocalDate.now().minusYears(userProperties.minimumAge()));
    }
}
