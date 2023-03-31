package com.kosnik.service.impl;

import com.kosnik.domain.Role;
import com.kosnik.domain.User;
import com.kosnik.service.AuthenticationService;
import com.kosnik.service.dto.AuthenticationRequest;
import com.kosnik.service.dto.AuthenticationResponse;
import com.kosnik.service.dto.UserDTO;
import com.kosnik.service.jwt.JwtService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.client.RestTemplate;

import javax.naming.AuthenticationException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@SpringBootTest
class AuthenticationServiceImplTest {

    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private RestTemplate restTemplate;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private final AuthenticationService authenticationService;
    @Autowired
    private static PasswordEncoder passwordEncoder;
    @Autowired
    private static ModelMapper modelMapper;

    private static AuthenticationRequest request;
    private static User user;
    private static UserDTO userDTORequest;
    private static UserDTO userDTOCreated;

    AuthenticationServiceImplTest(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @BeforeAll
    public static void setUp(){
        request =  new AuthenticationRequest("example123@mail.ru", "testExample");
        userDTORequest = UserDTO.builder().email("example123@mail.ru").password("testExample").build();
        user = User.builder().id("1").email("example123@mail.ru").password(passwordEncoder.encode("testExample")).role(Role.USER).build();
        userDTOCreated = modelMapper.map(user, UserDTO.class);
    }
    @Test
    void authenticateSuccessfullyResult() {
        doNothing().when(authenticationManager.authenticate(isA(UsernamePasswordAuthenticationToken.class)));
        when(restTemplate.getForObject(isA(String.class), UserDTO.class, request.email()))
                .thenReturn(UserDTO.builder().email(request.email()).build());

        AuthenticationResponse authResponse = authenticationService.authenticate(request);
        String username = jwtService.extractUsername(authResponse.getToken());

        assertEquals(username, request.email());
    }

    @Test
    void authenticateUserNotExistThrowAuthenticationException() {
        //TODO need or not, test?
        doNothing().when(restTemplate.getForObject(isA(String.class), UserDTO.class, request.email()));
        when(authenticationManager.authenticate(isA(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(AuthenticationException.class);

        assertThrows(AuthenticationException.class, () -> authenticationService.authenticate(request));
    }

    @Test
    void authenticateProblemWithUserServiceRuntimeException() {
        doNothing().when(authenticationManager.authenticate(isA(UsernamePasswordAuthenticationToken.class)));
        when(restTemplate.getForObject(isA(String.class), UserDTO.class, isA(String.class)))
                .thenThrow(RuntimeException.class);

        assertThrows(RuntimeException.class, () -> authenticationService.authenticate(request));
    }

    @Test
    void registerSuccessfullyResult() {
        when(restTemplate.postForObject(isA(String.class), HttpMethod.POST ,UserDTO.class, user))
                .thenReturn(userDTOCreated);

        AuthenticationResponse authResponse = authenticationService.register(userDTORequest);
        String username = jwtService.extractUsername(authResponse.getToken());

        assertEquals(username, userDTORequest.getEmail());
    }

    @Test
    void registerProblemWithUserServiceRuntimeException() {
        when(restTemplate.postForObject(isA(String.class), HttpMethod.POST ,UserDTO.class, isA(User.class)))
                .thenThrow(RuntimeException.class);

        assertThrows(RuntimeException.class, () -> authenticationService.register(userDTORequest));
    }

}