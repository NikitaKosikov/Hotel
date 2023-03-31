package com.kosnik.web.exception;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import com.kosnik.service.exception.RoomNotFoundException;

import java.net.URI;

@ControllerAdvice
public class ExceptionHandlerAdvice extends ResponseEntityExceptionHandler {
    @Value(value = "spring.application.name")
    private String ApplicationName;
    @ExceptionHandler(RoomNotFoundException.class)
    public ProblemDetail roomNotFoundException(RoomNotFoundException e) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, e.getMessage());
        problemDetail.setTitle("Room Not Found");
        problemDetail.setType(URI.create("https://" + ApplicationName + "/errors/not-found"));
        return problemDetail;
    }

}
