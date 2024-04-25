package pawlin.userapi.dto.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = LegalDateValidator.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface LegalDate {
    String message() default "Age must be legal";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
