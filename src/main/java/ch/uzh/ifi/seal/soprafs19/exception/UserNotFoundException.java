package ch.uzh.ifi.seal.soprafs19.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class UserNotFoundException extends RuntimeException {

    private long idNotFound;

    public UserNotFoundException(long id){
        this.idNotFound = id;
    }

    public long getIdNotFound() {
        return idNotFound;
    }

    public void setIdNotFound(long idNotFound) {
        this.idNotFound = idNotFound;
    }
}
