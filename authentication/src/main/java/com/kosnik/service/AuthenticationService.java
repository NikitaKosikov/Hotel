package com.kosnik.service;

import com.kosnik.service.dto.AuthenticationRequest;
import com.kosnik.service.dto.AuthenticationResponse;
import com.kosnik.service.dto.UserDTO;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface AuthenticationService{
    AuthenticationResponse authenticate(AuthenticationRequest request);
    AuthenticationResponse register(UserDTO userDTO);
}
