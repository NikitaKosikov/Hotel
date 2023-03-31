package com.kosnik.service;

import com.kosnik.domain.User;
import com.kosnik.service.dto.UserDTO;
import com.kosnik.service.exception.UserAlreadyExistException;
import com.kosnik.service.exception.UserNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Map;

public interface UserService {
    Page<User> findAll(Map<String, String> params, Pageable pageable);

    User find(String id) throws UserNotFoundException;

    User update(UserDTO updateUserDTO, String id) throws UserAlreadyExistException, UserNotFoundException;

    User save(UserDTO userDTO) throws UserAlreadyExistException;

    User findByEmail(String email) throws UserNotFoundException;
}
