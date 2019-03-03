package ch.uzh.ifi.seal.soprafs19.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class InvalidCredentialsException extends RuntimeException {
}
