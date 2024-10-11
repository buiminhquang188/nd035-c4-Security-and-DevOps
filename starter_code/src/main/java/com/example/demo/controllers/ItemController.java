package com.example.demo.controllers;

import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/item")
public class ItemController {
    private static final Logger log = LoggerFactory.getLogger(ItemController.class);

    @Autowired
    private ItemRepository itemRepository;

    @GetMapping
    public ResponseEntity<List<Item>> getItems() {
        log.info("Fetching all items");
        List<Item> items = itemRepository.findAll();
        log.info("Items fetched: {}", items);

        return ResponseEntity.ok(items);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Item> getItemById(@PathVariable Long id) {
        log.info("Fetching item by id: {}", id);
        Optional<Item> item = itemRepository.findById(id);

        if (!item.isPresent()) {
            log.warn("Item not found: {}", id);
            return ResponseEntity.notFound()
                    .build();
        }

        log.info("Item fetched: {}", item);
        return ResponseEntity.of(item);
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<List<Item>> getItemsByName(@PathVariable String name) {
        log.info("Fetching item by name: {}", name);
        List<Item> items = itemRepository.findByName(name);

        if (items == null || items.isEmpty()) {
            log.warn("Item not found: {}", name);
            return ResponseEntity.notFound()
                    .build();
        }

        log.error("Item fetched: {}", items);
        return ResponseEntity.ok(items);
    }

}
