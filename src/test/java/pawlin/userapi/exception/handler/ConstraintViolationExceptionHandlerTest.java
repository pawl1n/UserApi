package pawlin.userapi.exception.handler;

import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;

import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

class ConstraintViolationExceptionHandlerTest {
    private final ConstraintViolationExceptionHandler constraintViolationExceptionHandler = new ConstraintViolationExceptionHandler();

    @Test
    void shouldHandleConstraintViolationException() {
        // given
        ConstraintViolationException exception = new ConstraintViolationException(new HashSet<>());

        // when
        ResponseEntity<Object> response = constraintViolationExceptionHandler.handleConstraintViolationException(exception, null);

        // then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertInstanceOf(ProblemDetail.class, response.getBody());
    }
}