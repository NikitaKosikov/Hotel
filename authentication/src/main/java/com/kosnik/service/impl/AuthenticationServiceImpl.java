package com.kosnik.service.impl;

import com.kosnik.domain.Role;
import com.kosnik.domain.User;
import com.kosnik.service.AuthenticationService;
import com.kosnik.service.dto.AuthenticationRequest;
import com.kosnik.service.dto.AuthenticationResponse;
import com.kosnik.service.dto.UserDTO;
import com.kosnik.service.jwt.JwtService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService{
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final RestTemplate restTemplate;
    private final ModelMapper modelMapper;

    private final static String USER_API_URL = "http://USER-CLIENT/api/v1/users";
    public AuthenticationResponse authenticate(AuthenticationRequest request){
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.email(),
                        request.password()
                )
        );

        UserDTO userDTO = restTemplate.getForObject(USER_API_URL, UserDTO.class, request.email());
        User user = modelMapper.map(userDTO, User.class);
        String jwt = jwtService.generateToken(user);
        return AuthenticationResponse.builder().token(jwt).build();
    }
    public AuthenticationResponse register(UserDTO userDTO) {
        User user = User.builder()
                .email(userDTO.getEmail())
                .password(passwordEncoder.encode(userDTO.getPassword()))
                .role(Role.USER)
                .build();

        userDTO = restTemplate.postForObject(USER_API_URL, HttpMethod.POST, UserDTO.class, user);
        user = modelMapper.map(userDTO, User.class);
        String jwt = jwtService.generateToken(user);
        return AuthenticationResponse.builder().token(jwt).build();
    }
}
