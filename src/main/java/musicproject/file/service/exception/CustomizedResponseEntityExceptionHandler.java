package musicproject.file.service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;

@ControllerAdvice
@RestController
public class CustomizedResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    /*500 Internal Server Error Exception Handler -> Generic */
    @ExceptionHandler(Exception.class)
    public final ResponseEntity<ExceptionResponse> handleAllExceptions(Exception exception, WebRequest webRequest) {

        ExceptionResponse response = new ExceptionResponse(new Date(), exception.getMessage(), webRequest.getDescription(false));

        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /*500 Internal Server Exception Handler -> File processing */
    @ExceptionHandler(FileProcessingException.class)
    public final ResponseEntity<ExceptionResponse> handleFileProcessingException(FileProcessingException exception, WebRequest webRequest) {

        ExceptionResponse response = new ExceptionResponse(new Date(), exception.getMessage(), webRequest.getDescription(false));

        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /*400 Bad Request Exception Handler -> Missing file */
    @ExceptionHandler(MissingFileParameterException.class)
    public final ResponseEntity<ExceptionResponse> handleMissingFileParameterException(MissingFileParameterException exception, WebRequest webRequest) {

        ExceptionResponse response = new ExceptionResponse(new Date(), exception.getMessage(), webRequest.getDescription(false));

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    /*404 Not Found Exception Handler -> File not found */
    @ExceptionHandler(FileNotFoundException.class)
    public final ResponseEntity<ExceptionResponse> handleMissingFilePException(FileNotFoundException exception, WebRequest webRequest) {

        ExceptionResponse response = new ExceptionResponse(new Date(), exception.getMessage(), webRequest.getDescription(false));

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

}
