package com.example.demo.controllers;

import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/order")
public class OrderController {
    private static final Logger log = LoggerFactory.getLogger(OrderController.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrderRepository orderRepository;

    @PostMapping("/submit/{username}")
    public ResponseEntity<UserOrder> submit(@PathVariable String username) {
        log.info("Submit order for user: {}", username);
        User user = userRepository.findByUsername(username);
        log.info("User found: {}", user);

        if (user == null) {
            log.warn("User not found: {}", username);
            return ResponseEntity.notFound()
                    .build();
        }

        log.info("Creating order for user: {}", username);
        UserOrder order = UserOrder.createFromCart(user.getCart());

        log.info("Saving order for user: {}", username);
        UserOrder userOrder = orderRepository.save(order);
        log.info("Order saved for user: {}", userOrder.getId());

        return ResponseEntity.ok(order);
    }

    @GetMapping("/history/{username}")
    public ResponseEntity<List<UserOrder>> getOrdersForUser(@PathVariable String username) {
        log.info("Get orders for user: {}", username);
        User user = userRepository.findByUsername(username);
        log.info("User found: {}", user);

        if (user == null) {
            log.warn("User not found: {}", username);
            return ResponseEntity.notFound()
                    .build();
        }

        log.info("Returning orders for user: {}", username);
        return ResponseEntity.ok(orderRepository.findByUser(user));
    }
}
