package com.kosnik.service.jwt;

import com.kosnik.domain.User;
import com.kosnik.service.dto.UserDTO;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final RestTemplate restTemplate;
    private final ModelMapper modelMapper;
    //TODO test?
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDTO userDTO = restTemplate.getForObject("http://USER-CLIENT/api/v1/users", UserDTO.class, username);
        return  modelMapper.map(userDTO, User.class);
    }
}
