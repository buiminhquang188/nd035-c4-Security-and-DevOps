package com.example.demo;

import com.example.demo.controllers.CartController;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

public class CartControllerTests {
    @Mock
    private UserRepository userRepository;

    @Mock
    private CartRepository cartRepository;

    @Mock
    private ItemRepository itemRepository;

    @InjectMocks
    private CartController cartController;

    public CartControllerTests() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testAddToCard_whenGivenInvalidUsername_thenReturnsNotFound() {
        String username = "test";
        when(userRepository.findByUsername(username)).thenReturn(null);

        ModifyCartRequest request = new ModifyCartRequest();
        request.setUsername(username);

        ResponseEntity<Cart> response = cartController.addTocart(request);

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testAddToCard_whenGivenInvalidItemId_thenReturnNotFound() {
        String username = "test";
        when(userRepository.findByUsername(username)).thenReturn(new User());

        ModifyCartRequest request = new ModifyCartRequest();
        request.setUsername(username);
        request.setItemId(1L);

        when(itemRepository.findById(request.getItemId())).thenReturn(Optional.empty());

        ResponseEntity<Cart> response = cartController.addTocart(request);

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testAddToCard_whenGivenValidRequest_thenReturnCart() {
        String username = "test";
        when(userRepository.findByUsername(username)).thenReturn(new User());

        ModifyCartRequest request = new ModifyCartRequest();
        request.setUsername(username);
        request.setItemId(1L);

        when(itemRepository.findById(request.getItemId())).thenReturn(Optional.of(new Item()));

        ResponseEntity<Cart> response = cartController.addTocart(request);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testRemoveFromCart_whenGivenInvalidUsername_thenReturnsNotFound() {
        String username = "test";
        when(userRepository.findByUsername(username)).thenReturn(null);

        ModifyCartRequest request = new ModifyCartRequest();
        request.setUsername(username);

        ResponseEntity<Cart> response = cartController.removeFromcart(request);

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testRemoveFormCart_whenGivenInvalidUsername_thenReturnsNotFound() {
        String username = "test";
        when(userRepository.findByUsername(username)).thenReturn(new User());

        ModifyCartRequest request = new ModifyCartRequest();
        request.setUsername(username);
        request.setItemId(1L);

        when(itemRepository.findById(request.getItemId())).thenReturn(Optional.empty());

        ResponseEntity<Cart> response = cartController.removeFromcart(request);

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testRemoveFromCart_whenGivenValidRequest_thenReturnCart() {
        String username = "test";
        when(userRepository.findByUsername(username)).thenReturn(new User());

        ModifyCartRequest request = new ModifyCartRequest();
        request.setUsername(username);
        request.setItemId(1L);

        when(itemRepository.findById(request.getItemId())).thenReturn(Optional.of(new Item()));

        ResponseEntity<Cart> response = cartController.removeFromcart(request);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}
