package ttb.assg.common;

import jakarta.persistence.EntityNotFoundException;
import lombok.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalAdviceHandler extends ResponseEntityExceptionHandler {


    private static final Logger logger = LoggerFactory.getLogger(GlobalAdviceHandler.class);

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException e,
                                                                  @NonNull HttpHeaders headers, @NonNull HttpStatusCode status, @NonNull WebRequest request) {
        var text = "Invalid arguments request";
        ProblemDetail detail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, text);
        Map<String, Object> properties = new HashMap<>();
        var invalidArgumentList = e.getBindingResult().getFieldErrors();
        for (var oe : invalidArgumentList) {
            properties.put(oe.getField(), oe.getDefaultMessage());
        }
        detail.setProperty("arguments", properties);
        return ResponseEntity.badRequest().body(detail);
    }

    @ExceptionHandler(NotFoundException.class)
    protected ResponseEntity<Object> notFoundExceptionHandler(NotFoundException e) {
        ProblemDetail detail = ProblemDetail.forStatusAndDetail(
                HttpStatus.NOT_FOUND,
                e.getMessage());
       logger.error("NotFoundException: {}", detail);
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(ValidationException.class)
    protected ResponseEntity<Object> validationExceptionHandler(EntityNotFoundException e) {
        ProblemDetail detail = ProblemDetail.forStatusAndDetail(
                HttpStatus.NOT_FOUND,
                e.getMessage());
       logger.error("Validation error: {}", detail);
        return ResponseEntity.badRequest().body(detail);
    }


    @ExceptionHandler(Exception.class)
    protected ResponseEntity<Object> globalExceptionHandling(Exception e) {
        ProblemDetail detail = ProblemDetail.forStatusAndDetail(
                HttpStatus.INTERNAL_SERVER_ERROR,
                e.getMessage());
        logger.error(e.getMessage());
        return ResponseEntity.internalServerError()
                .body(detail);
    }
}
