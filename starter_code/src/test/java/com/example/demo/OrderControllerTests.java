package com.example.demo;

import com.example.demo.controllers.OrderController;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

public class OrderControllerTests {
    @Mock
    private UserRepository userRepository;

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderController orderController;

    public OrderControllerTests() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testSubmit_whenUsernameGiven_thenOrderReturned() {
        User user = new User();
        user.setId(1L);
        user.setUsername("test");
        user.setPassword("testPassword");

        Cart cart = new Cart();
        cart.setItems(new ArrayList<>());
        cart.setTotal(BigDecimal.valueOf(10));
        cart.setItems(IntStream.range(0, 9)
                .mapToObj(i -> {
                    Item item = new Item();
                    item.setName("item" + i);
                    item.setPrice(BigDecimal.valueOf(i));
                    return item;
                })
                .collect(Collectors.toList()));

        user.setCart(cart);

        UserOrder order = UserOrder.createFromCart(cart);
        order.setId(1L);

        when(userRepository.findByUsername("test")).thenReturn(user);
        when(orderRepository.save(order)).thenReturn(order);

        ResponseEntity<UserOrder> response = orderController.submit("test");

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(order.getTotal(), response.getBody()
                .getTotal());
        assertEquals(order.getItems()
                .get(0)
                .getName(), response.getBody()
                .getItems()
                .get(0)
                .getName());
    }

    @Test
    public void testSubmit_whenUsernameGiven_thenUserNotFound() {
        when(userRepository.findByUsername("test")).thenReturn(null);

        ResponseEntity<UserOrder> response = orderController.submit("test");

        assertNotNull(response);
        assertEquals(response.getStatusCode(), HttpStatus.NOT_FOUND);
    }

    @Test
    public void testGetOrdersForUser_whenUsernameGiven_thenOrdersReturned() {
        User user = new User();
        user.setId(1L);
        user.setUsername("test");
        user.setPassword("testPassword");

        Cart cart = new Cart();
        cart.setItems(new ArrayList<>());
        cart.setTotal(BigDecimal.valueOf(10));
        cart.setItems(IntStream.range(0, 9)
                .mapToObj(i -> {
                    Item item = new Item();
                    item.setName("item" + i);
                    item.setPrice(BigDecimal.valueOf(i));
                    return item;
                })
                .collect(Collectors.toList()));

        user.setCart(cart);

        UserOrder order = UserOrder.createFromCart(cart);
        order.setId(1L);

        when(userRepository.findByUsername("test")).thenReturn(user);
        when(orderRepository.findByUser(user)).thenReturn(new ArrayList<UserOrder>() {{
            add(order);
        }});

        ResponseEntity<List<UserOrder>> response = orderController.getOrdersForUser("test");

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody()
                .size());
        assertEquals(order.getTotal(), response.getBody()
                .get(0)
                .getTotal());
        assertEquals(order.getItems()
                .get(0)
                .getName(), response.getBody()
                .get(0)
                .getItems()
                .get(0)
                .getName());
    }

    @Test
    public void testGetOrdersForUser_whenUsernameGiven_thenUserNotFound() {
        when(userRepository.findByUsername("test")).thenReturn(null);

        ResponseEntity<List<UserOrder>> response = orderController.getOrdersForUser("test");

        assertNotNull(response);
        assertEquals(response.getStatusCode(), HttpStatus.NOT_FOUND);
    }
}
