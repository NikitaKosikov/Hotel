package com.kosnik.service.impl;

import com.kosnik.service.UserService;
import com.kosnik.service.converter.SearchQueryConverter;
import com.kosnik.domain.SearchQuery;
import lombok.RequiredArgsConstructor;
import com.kosnik.domain.Role;
import com.kosnik.domain.User;
import com.kosnik.repository.UserRepository;
import com.kosnik.service.dto.UserDTO;
import com.kosnik.service.exception.UserAlreadyExistException;
import com.kosnik.service.exception.UserNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Page<User> findAll(Map<String,String> params, Pageable pageable) {
        SearchQuery searchQuery = SearchQueryConverter.convert(params);
        return userRepository.findAll(searchQuery, pageable);
    }
    @Override
    public User find(String id) throws UserNotFoundException {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User with id=" + id + " doesn't exist"));
    }
    @Override
    @Transactional
    public User save(UserDTO userDTO) throws UserAlreadyExistException {
        checkUserAlreadyExist(userDTO.getEmail());
        User user = User.builder()
                .email(userDTO.getEmail())
                .password(passwordEncoder.encode(userDTO.getPassword()))
                .role(Role.USER)
                .build();
        user = userRepository.save(user);
        return user;
    }
    @Override
    @Transactional
    public User update(UserDTO updateUserDTO, String id) throws UserAlreadyExistException, UserNotFoundException {
        User user = find(id);
        if (!user.sameEmail(updateUserDTO.getEmail())){
            checkUserAlreadyExist(updateUserDTO.getEmail());
        }
        user.setEmail(updateUserDTO.getEmail());
        user.setPassword(passwordEncoder.encode(updateUserDTO.getPassword()));
        user = userRepository.save(user);
        return user;
    }
    @Override
    public User findByEmail(String email) throws UserNotFoundException {
        return userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException("User not found"));
    }
    private void checkUserAlreadyExist(String email) throws UserAlreadyExistException {
        try {
            findByEmail(email);
        }catch (UserNotFoundException e){
            return;
        }
        throw new UserAlreadyExistException("User already exist");
    }
}
