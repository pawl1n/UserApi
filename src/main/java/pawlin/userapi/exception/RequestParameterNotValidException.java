package pawlin.userapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.lang.NonNull;
import org.springframework.web.ErrorResponse;

public class RequestParameterNotValidException extends RuntimeException implements ErrorResponse {
    private final ProblemDetail body;

    public RequestParameterNotValidException(String message) {
        super(message);
        this.body = ProblemDetail.forStatusAndDetail(this.getStatusCode(), message);
    }

    @Override
    @NonNull
    public HttpStatusCode getStatusCode() {
        return HttpStatusCode.valueOf(HttpStatus.BAD_REQUEST.value());
    }

    @Override
    @NonNull
    public ProblemDetail getBody() {
        return this.body;
    }
}
