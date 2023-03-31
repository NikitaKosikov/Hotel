package com.kosnik.service.impl;

import com.kosnik.domain.Role;
import com.kosnik.domain.User;
import com.kosnik.repository.UserRepository;
import com.kosnik.service.dto.UserDTO;
import com.kosnik.service.exception.UserAlreadyExistException;
import com.kosnik.service.exception.UserNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private UserServiceImpl sut;
    @Mock
    private PasswordEncoder passwordEncoderMock;
    private final PasswordEncoder passwordEncoderTest = new BCryptPasswordEncoder(12);

    @Test
    void userFoundById() throws UserNotFoundException {
        String id = "1";
        when(userRepository.findById(anyString())).thenReturn(Optional.of(User.builder().id(id).build()));

        User actualUser = sut.find(id);

        assertNotNull(actualUser);
        assertEquals(id, actualUser.getId());
    }
    @Test
    void userNotFoundById(){
        when(userRepository.findById(anyString())).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> sut.find("1"));
    }
    @Test
    void userIsSavedInDB() throws UserAlreadyExistException {
        UserDTO incomingUserDTO = UserDTO.builder().email("example@mail.ru").password("test").build();
        String expectedPassword = passwordEncoderTest.encode(incomingUserDTO.getPassword());
        User savedUser = User.builder().email("example@mail.ru").password(expectedPassword).role(Role.USER).build();
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenReturn(savedUser);
        when(passwordEncoderMock.encode(incomingUserDTO.getPassword())).thenReturn(expectedPassword);

        User actualUser = sut.save(incomingUserDTO);

        assertNotNull(actualUser);
        assertEquals(Role.USER, actualUser.getRole());
        assertEquals(expectedPassword, actualUser.getPassword());
    }
    @Test
    void whenSavingUserTheUserAlreadyExist() {
        UserDTO incomingUserDTO = UserDTO.builder().email("example@mail.ru").password("test").build();
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(new User()));

        assertThrowsExactly(UserAlreadyExistException.class, ()-> sut.save(incomingUserDTO));
    }
    @Test
    void whenUpdatingUserTheNewEmailHasChanged() throws UserNotFoundException, UserAlreadyExistException {
        UserDTO incomingUserDTO = UserDTO.builder().email("example2@mail.ru").password("test").build();
        String expectedPassword = passwordEncoderTest.encode(incomingUserDTO.getPassword());
        User userFoundById = User.builder().email("example@mail.ru").password(expectedPassword).role(Role.USER).build();
        User savedUser = User.builder().email("example2@mail.ru").password(expectedPassword).role(Role.USER).build();
        when(userRepository.findById(anyString())).thenReturn(Optional.of(userFoundById));
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenReturn(savedUser);
        when(passwordEncoderMock.encode(incomingUserDTO.getPassword())).thenReturn(expectedPassword);

        User actualUser = sut.update(incomingUserDTO, "1");

        assertEquals(incomingUserDTO.getEmail(), actualUser.getEmail());
        assertEquals(expectedPassword, actualUser.getPassword());
    }
    @Test
    void whenUpdatingUserTheNewEmailHasNotChanged() throws UserNotFoundException, UserAlreadyExistException {
        UserDTO incomingUserDTO = UserDTO.builder().email("example@mail.ru").password("test").build();
        String expectedPassword = passwordEncoderTest.encode(incomingUserDTO.getPassword());
        User outgoingUser = User.builder().email("example@mail.ru").password(expectedPassword).role(Role.USER).build();
        User userFoundById = User.builder().email("example@mail.ru").password(expectedPassword).role(Role.USER).build();
        when(userRepository.findById(anyString())).thenReturn(Optional.of(userFoundById));
        when(userRepository.save(any(User.class))).thenReturn(outgoingUser);
        when(passwordEncoderMock.encode(incomingUserDTO.getPassword())).thenReturn(expectedPassword);

        User actualUser = sut.update(incomingUserDTO, "1");

        assertEquals(incomingUserDTO.getEmail(), actualUser.getEmail());
        assertEquals(expectedPassword, actualUser.getPassword());
    }
    @Test
    void whenUpdatingUserTheUserNotFoundById(){
        UserDTO incomingUserDTO = UserDTO.builder().email("example@mail.ru").password("test").build();
        when(userRepository.findById(anyString())).thenReturn(Optional.empty());

        assertThrowsExactly(UserNotFoundException.class, ()-> sut.update(incomingUserDTO, "1"));
    }
    @Test
    void whenUpdatingUserTheNewEmailAlreadyTaken(){
        User ownUserInDB = User.builder().email("example@mail.ru").build();
        when(userRepository.findById(anyString())).thenReturn(Optional.of(ownUserInDB));
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(new User()));

        assertThrowsExactly(UserAlreadyExistException.class,
                ()-> sut.update(UserDTO.builder().email("example2@mail.ru").build(), "1"));
    }
    @Test
    void findUserByEmail() throws UserNotFoundException {
        String incomingEmail = "example@mail.ru";
        User outgoingUser = User.builder().email(incomingEmail).build();
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(outgoingUser));

        User actualUser = sut.findByEmail(incomingEmail);

        assertEquals(incomingEmail, actualUser.getEmail());
    }
    @Test
    void findUserByEmailTheUserNotFound(){
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, ()-> sut.findByEmail("example@mail.ru"));
    }

}