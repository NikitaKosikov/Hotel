package com.kosnik.web.exception;

import com.kosnik.domain.Competitor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.net.URI;
import java.util.concurrent.CompletableFuture;

@RestControllerAdvice
public class ExceptionHandlerAdvice extends ResponseEntityExceptionHandler {
    @Value(value = "spring.application.name")
    private String applicationName;

    public CompletableFuture<ProblemDetail> fallbackMethod(Competitor competitor, RuntimeException e) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, e.getMessage());
        problemDetail.setTitle("Something went wrong in competitor-service");
        problemDetail.setType(URI.create("https://" + applicationName + "/errors"));
        return CompletableFuture.supplyAsync(()->problemDetail);
    }
}
