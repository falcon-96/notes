package com.sdx.notes.advice;

import com.sdx.notes.exception.AccessDeniedException;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class ExceptionHandlerAdvice {
    private static final Logger log = LoggerFactory.getLogger(ExceptionHandlerAdvice.class);

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ErrorDetails> entityNotFoundHandling(Exception exception) {
        log.error(exception.getMessage());
        return new ResponseEntity<>(new ErrorDetails(LocalDateTime.now(),
                exception.getLocalizedMessage()),
                HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<ErrorDetails> accessDeniedException(Exception exception) {
        log.error(exception.getMessage());
        return new ResponseEntity<>(new ErrorDetails(LocalDateTime.now(),
                exception.getLocalizedMessage()),
                HttpStatus.UNAUTHORIZED);
    }
}
