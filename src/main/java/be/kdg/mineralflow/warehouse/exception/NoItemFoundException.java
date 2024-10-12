package be.kdg.mineralflow.warehouse.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class NoItemFoundException extends RuntimeException {
    public NoItemFoundException(String message) {
        super(message);
    }
}
