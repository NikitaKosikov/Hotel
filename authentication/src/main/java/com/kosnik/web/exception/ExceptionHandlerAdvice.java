package com.kosnik.web.exception;

import com.kosnik.service.dto.UserDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.naming.AuthenticationException;
import java.net.URI;
import java.nio.file.AccessDeniedException;
import java.util.concurrent.CompletableFuture;

@RestControllerAdvice
public class ExceptionHandlerAdvice extends ResponseEntityExceptionHandler {
    @Value(value = "spring.application.name")
    private String applicationName;
    @ExceptionHandler(AuthenticationException.class)
    public ProblemDetail handleAuthenticationException(AuthenticationException e) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED, e.getMessage());
        problemDetail.setTitle("User Unauthorized");
        problemDetail.setType(URI.create("https://" + applicationName + "/errors/not-found"));
        return problemDetail;
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ProblemDetail handleAccessDeniedException(AccessDeniedException e) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.FORBIDDEN, e.getMessage());
        problemDetail.setTitle("User Access Denied");
        problemDetail.setType(URI.create("https://" + applicationName + "/errors/not-found"));
        return problemDetail;
    }

    public CompletableFuture<ProblemDetail> fallbackMethod(UserDTO userDTO, RuntimeException e) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, e.getMessage());
        problemDetail.setTitle("Something went wrong in user-service");
        problemDetail.setType(URI.create("https://" + applicationName + "/errors"));
        return CompletableFuture.supplyAsync(()->problemDetail);
    }
}
