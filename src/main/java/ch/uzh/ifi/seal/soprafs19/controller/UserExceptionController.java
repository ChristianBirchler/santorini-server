package ch.uzh.ifi.seal.soprafs19.controller;

import ch.uzh.ifi.seal.soprafs19.exception.UserAlreadyExistsException;
import ch.uzh.ifi.seal.soprafs19.exception.UserNotFoundException;
import ch.uzh.ifi.seal.soprafs19.response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class UserExceptionController {




    @ExceptionHandler(UserNotFoundException.class)
    public final ResponseEntity<Object> handleUserNotFoundException(UserNotFoundException ex){

        String idString = Long.toString(ex.getIdNotFound());
        ErrorResponse error = new ErrorResponse("user with userId=" + idString + " not found");
        return new ResponseEntity(error, HttpStatus.NOT_FOUND);

    }


    @ExceptionHandler(UserAlreadyExistsException.class)
    public final ResponseEntity<Object> handleUserAlreadyExistsException(){

        ErrorResponse error = new ErrorResponse("add User failed because username already exists");

        return new ResponseEntity(error, HttpStatus.CONFLICT);
    }




}
