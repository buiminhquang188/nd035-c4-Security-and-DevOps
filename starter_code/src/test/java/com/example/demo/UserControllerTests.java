package com.example.demo;

import com.example.demo.controllers.UserController;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

public class UserControllerTests {

    @Mock
    private UserRepository userRepository;

    @Mock
    private CartRepository cartRepository;

    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @InjectMocks
    private UserController userController;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testFindById() {
        User user = new User();
        user.setId(1L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        ResponseEntity<User> response = userController.findById(1L);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(user.getId(), response.getBody()
                .getId());
    }

    @Test
    public void testFindByUserName_thenReturnUser() {
        User user = new User();
        user.setUsername("testUser");
        when(userRepository.findByUsername("testUser")).thenReturn(user);

        ResponseEntity<User> response = userController.findByUserName("testUser");

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(user.getUsername(), response.getBody()
                .getUsername());
    }

    @Test
    public void testFindByUserName_thenReturnNotFound() {
        User user = new User();
        user.setUsername("testUser");
        when(userRepository.findByUsername("testUser")).thenReturn(null);

        ResponseEntity<User> response = userController.findByUserName("testUser");

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    public void testCreateUser_thenReturnCreateSuccess() {
        CreateUserRequest request = new CreateUserRequest();
        request.setUsername("testUser");
        request.setPassword("testPassword");
        request.setConfirmPassword("testPassword");

        when(bCryptPasswordEncoder.encode("testPassword")).thenReturn("hashedPassword");

        User user = new User();
        user.setId(1L);
        user.setUsername(request.getUsername());
        user.setPassword(bCryptPasswordEncoder.encode(request.getPassword()));

        when(userRepository.save(user)).thenReturn(user);
        ResponseEntity<User> response = userController.createUser(request);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("testUser", response.getBody()
                .getUsername());
        assertEquals("hashedPassword", response.getBody()
                .getPassword());
    }

    @Test
    public void testCreateUser_whenConfirmPasswordInvalid_thenReturnCreateBadRequest() {
        CreateUserRequest request = new CreateUserRequest();
        request.setUsername("testUser");
        request.setPassword("testPassword");
        request.setConfirmPassword("invalidConfirmPassword");

        ResponseEntity<User> response = userController.createUser(request);

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    public void testCreateUser_whenPasswordLengthSizeIsLessThan7_thenReturnCreateBadRequest() {
        CreateUserRequest request = new CreateUserRequest();
        request.setUsername("testUser");
        request.setPassword("1");
        request.setConfirmPassword("invalidConfirmPassword");

        ResponseEntity<User> response = userController.createUser(request);

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNull(response.getBody());
    }
}