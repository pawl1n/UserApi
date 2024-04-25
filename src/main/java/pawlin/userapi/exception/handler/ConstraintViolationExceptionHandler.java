package pawlin.userapi.exception.handler;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class ConstraintViolationExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> handleConstraintViolationException(ConstraintViolationException e, WebRequest request) {
        ProblemDetail body = ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(400), e.getMessage());
        return handleExceptionInternal(e, body, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }
}
