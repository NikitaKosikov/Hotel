package com.kosnik.web.controller;

import com.kosnik.service.AuthenticationService;
import com.kosnik.service.dto.AuthenticationRequest;
import com.kosnik.service.dto.AuthenticationResponse;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import lombok.RequiredArgsConstructor;
import com.kosnik.service.dto.UserDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
@PreAuthorize("permitAll()")
public class AuthenticationController {
    private final AuthenticationService authenticateService;
    @PostMapping("/authenticate")
    @CircuitBreaker(name = "user", fallbackMethod = "fallbackMethod")
    @TimeLimiter(name = "user")
    @Retry(name = "user")
    public CompletableFuture<ResponseEntity<AuthenticationResponse>> authenticate(@RequestBody AuthenticationRequest request){
        AuthenticationResponse authenticateResponse = authenticateService.authenticate(request);
        return CompletableFuture.supplyAsync(() ->
                ResponseEntity.status(HttpStatus.CREATED.value()).body(authenticateResponse));
    }
    @PostMapping("/register")
    @CircuitBreaker(name = "user", fallbackMethod = "fallbackMethod")
    @TimeLimiter(name = "user")
    @Retry(name = "user")
    public CompletableFuture<ResponseEntity<AuthenticationResponse>> register(@RequestBody UserDTO request){
        return CompletableFuture.supplyAsync(()->ResponseEntity.ok(authenticateService.register(request)));
    }
}