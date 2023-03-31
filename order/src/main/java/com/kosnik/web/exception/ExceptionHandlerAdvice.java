package com.kosnik.web.exception;

import com.kosnik.service.dto.OrderDTO;
import com.kosnik.service.exception.OrderNotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.net.URI;
import java.util.concurrent.CompletableFuture;

//TODO exceptions handle add
@ControllerAdvice
public class ExceptionHandlerAdvice extends ResponseEntityExceptionHandler {
    @Value(value = "spring.application.name")
    private String ApplicationName;
    @ExceptionHandler(OrderNotFoundException.class)
    public ProblemDetail orderNotFoundException(OrderNotFoundException e) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, e.getMessage());
        problemDetail.setTitle("Order Not Found");
        problemDetail.setType(URI.create("https://" + ApplicationName + "/errors/not-found"));
        return problemDetail;
    }

    public CompletableFuture<ProblemDetail> fallbackMethodNotification(OrderDTO orderDTO, RuntimeException e) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, e.getMessage());
        problemDetail.setTitle("Something went wrong in notification-service");
        problemDetail.setType(URI.create("https://" + ApplicationName + "/errors"));
        return CompletableFuture.supplyAsync(()->problemDetail);
    }

}
