package musicproject.file.service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class MissingFileParameterException extends RuntimeException {

    public MissingFileParameterException(String message) {
        super(message);
    }
}
