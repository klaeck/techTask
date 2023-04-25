package com.klaeck.techtask.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class ControllerExceptionHandler {
    private static final String LOG_TEMPLATE = "Error thrown by controller. Request URL={}";
    private static final String LOG_PREFIX = "Error details: {}";

    private static final Logger LOGGER = LoggerFactory.getLogger(ControllerExceptionHandler.class);

    @ExceptionHandler(value = EmptyResultDataAccessException.class)
    public ResponseEntity<String> handleNotExistingEntity(HttpServletRequest request, EmptyResultDataAccessException ex){
        logError(request, ex);

        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = NotFoundException.class)
    public ResponseEntity<String> handleNotExistingEntity(HttpServletRequest request, NotFoundException ex){
        logError(request, ex);

        return new ResponseEntity<>("Entity not found", HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = DataIntegrityViolationException.class)
    public ResponseEntity<String> handlePostgreException(HttpServletRequest request, DataIntegrityViolationException ex) {
        logError(request, ex);
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<String> handleUnexpectedException (HttpServletRequest request, Exception e){
        logError(request, e);
        return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private void logError(HttpServletRequest request, Exception ex) {
        String url = request.getRequestURI() + "?" + request.getQueryString();
        LOGGER.error(LOG_TEMPLATE, url);
        LOGGER.error(LOG_PREFIX, ex.getMessage());
    }

}
