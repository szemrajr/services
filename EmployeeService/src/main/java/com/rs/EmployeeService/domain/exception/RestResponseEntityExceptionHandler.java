package com.rs.EmployeeService.domain.exception;

import org.redisson.api.RTopic;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    private static final String TOPIC = "error";

    @Autowired
    private RedissonClient redissonClient;

    @ExceptionHandler(value = { EmployeeWithSamePESELExistsException.class,
                                DirectorLimitExceededException.class,
                                ManagerSubordinatesLimitExceededException.class})
    protected ResponseEntity<Object> businessExceptionsHandler(RuntimeException ex, WebRequest request) {
        String typeAndMessage = ex.getClass().toString() + " : " + ex.getMessage();
        RuntimeException exception = new RuntimeException(typeAndMessage);
        exception.setStackTrace(ex.getStackTrace());

        RTopic publishTopic = redissonClient.getTopic(TOPIC);
        publishTopic.publish(exception);
        return handleExceptionInternal(ex, ex.getMessage(), new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(value = RuntimeException.class)
    protected ResponseEntity<Object> otherExceptionsHandler(RuntimeException ex, WebRequest request) {
        return handleExceptionInternal(ex, ex.getMessage(), new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        String errorMessage = ex.getBindingResult().getFieldErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .reduce("", (subtotal, element) -> subtotal + element + "\n");
        return handleExceptionInternal(ex, errorMessage, headers, HttpStatus.BAD_REQUEST, request);
    }
}