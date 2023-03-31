package com.kosnik.web.controller;

import com.kosnik.domain.User;
import com.kosnik.service.UserService;
import com.kosnik.service.dto.UserDTO;
import com.kosnik.service.exception.UserAlreadyExistException;
import com.kosnik.service.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserService userService;
    private final ModelMapper modelMapper;
    @GetMapping
    public ResponseEntity<List<UserDTO>> findAll(
            @RequestParam(required = false) Map<String,String> params,
            Pageable pageable){
        Page<User> users = userService.findAll(params, pageable);
        List<UserDTO> usersDTO = modelMapper.map(users.getContent(), new TypeToken<List<UserDTO>>(){}.getType());
        return ResponseEntity.ok(usersDTO);
    }
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> find(@PathVariable("id") String id) throws UserNotFoundException {
        User user = userService.find(id);
        UserDTO userDTO = modelMapper.map(user, UserDTO.class);
        return ResponseEntity.ok(userDTO);
    }
    @PostMapping
    public ResponseEntity<UserDTO> save(@RequestBody UserDTO userDTO)  throws UserAlreadyExistException {
        User user = userService.save(userDTO);
        userDTO = modelMapper.map(user, UserDTO.class);
        return ResponseEntity.status(HttpStatus.CREATED).body(userDTO);
    }
    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> update(@PathVariable("id") String id, @RequestBody UserDTO userDTO) throws UserAlreadyExistException, UserNotFoundException {
        User user = userService.update(userDTO, id);
        userDTO = modelMapper.map(user, UserDTO.class);
        return ResponseEntity.ok(userDTO);
    }
}
