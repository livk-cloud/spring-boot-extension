package com.livk.redisson.limit.controller;

import com.livk.autoconfigure.limit.exception.LimitException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author livk
 */
@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler(LimitException.class)
    public HttpEntity<String> handleLimitException(LimitException e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
    }
}
