package be.kdg.mineralflow.warehouse.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * De Exception IncorrectDomainException is used when there is a manipulation performed
 * on the entities that do not adhere to domain rules
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class IncorrectDomainException extends RuntimeException{
    public IncorrectDomainException(String message) {
        super(message);
    }
}
