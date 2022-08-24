package com.tweetapp.exception;

import com.tweetapp.model.ResponseForIssue;

import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String,String> handleValidationException(MethodArgumentNotValidException ex){
        Map<String,String> errors=new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error-> {
            String fieldName=((FieldError)error).getField();
            String errorMessage=error.getDefaultMessage();
            errors.put(fieldName,errorMessage);
        });
        log.error("{}",errors);
        return errors;
    }
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)

    public List<String> handleConstraintValidationException(ConstraintViolationException ex){
        List<String> errors=new ArrayList<>();
        
        errors=ex.getConstraintViolations().stream().map(voilation -> voilation.getRootBeanClass().getName()).collect(Collectors.toList());

        log.error("{}",errors);
        return errors;
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<Object> handleUnauthorizedExceptions(UnauthorizedException ex) {
        log.error(ex.getMessage());
        return new ResponseEntity<>(
                new ResponseForIssue(ex.getMessage(), LocalDateTime.now(), HttpStatus.UNAUTHORIZED),
                HttpStatus.UNAUTHORIZED);

    }
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(UserExistsException.class)
    public ResponseEntity<Object> handleUserExistsExceptions(UserExistsException ex) {
        log.error(ex.getMessage());
        return new ResponseEntity<>(
                new ResponseForIssue(ex.getMessage(), LocalDateTime.now(), HttpStatus.BAD_REQUEST),
                HttpStatus.BAD_REQUEST);
    }
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleExceptions(Exception ex) {
        log.error(ex.getMessage());
        return new ResponseEntity<>(
                new ResponseForIssue(ex.getMessage(), LocalDateTime.now(), HttpStatus.INTERNAL_SERVER_ERROR),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
