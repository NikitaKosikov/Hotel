package com.kosnik.web.exception;

import com.kosnik.service.exception.UserAlreadyExistException;
import com.kosnik.service.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.net.URI;

@ControllerAdvice
public class ExceptionHandlerAdvice extends ResponseEntityExceptionHandler {

    @Value(value = "spring.application.name")
    private String ApplicationName;
    @ExceptionHandler(UserNotFoundException.class)
    public ProblemDetail userNotFoundException(UserNotFoundException e) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, e.getMessage());
        problemDetail.setTitle("User Not Found");
        problemDetail.setType(URI.create("https://" + ApplicationName + "/errors/not-found"));
        return problemDetail;
    }

    @ExceptionHandler(UserAlreadyExistException.class)
    public ProblemDetail userAlreadyExistException(UserAlreadyExistException e) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, e.getMessage());
        problemDetail.setTitle("User Already Exist");
        problemDetail.setType(URI.create("https://" + ApplicationName + "/errors/already-exist"));
        return problemDetail;
    }
}
