package pawlin.userapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.lang.NonNull;
import org.springframework.web.ErrorResponse;

public class EntityNotFoundException extends RuntimeException implements ErrorResponse {
    private final ProblemDetail body;

    public EntityNotFoundException(Long id) {
        super("Could not find entity with id " + id);
        this.body = ProblemDetail.forStatusAndDetail(this.getStatusCode(), "Could not find entity with id " + id);
    }

    @Override
    @NonNull
    public HttpStatusCode getStatusCode() {
        return HttpStatusCode.valueOf(HttpStatus.NOT_FOUND.value());
    }

    @Override
    @NonNull
    public ProblemDetail getBody() {
        return this.body;
    }
}
