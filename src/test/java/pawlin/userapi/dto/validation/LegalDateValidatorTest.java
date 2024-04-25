package pawlin.userapi.dto.validation;

import org.junit.jupiter.api.Test;
import pawlin.userapi.configuration.UserProperties;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class LegalDateValidatorTest {
    private final LegalDateValidator validator = new LegalDateValidator(new UserProperties(18));

    @Test
    void shouldBeValid() {
        // when
        boolean result = validator.isValid(LocalDate.now().minusYears(18), null);

        // then
        assertTrue(result);
    }

    @Test
    void shouldNotBeValid() {
        // when
        boolean result = validator.isValid(LocalDate.now().minusYears(17), null);

        // then
        assertFalse(result);
    }
}