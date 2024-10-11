package com.example.demo;

import com.example.demo.controllers.ItemController;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

public class ItemControllerTests {
    @Mock
    private ItemRepository itemRepository;

    @InjectMocks
    private ItemController itemController;

    public ItemControllerTests() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetItems() {
        Item item = new Item();
        item.setName("test");
        item.setDescription("test");
        item.setPrice(BigDecimal.valueOf(1.0));

        List<Item> items = new ArrayList<>();
        items.add(item);
        when(itemRepository.findAll()).thenReturn(items);

        ResponseEntity<List<Item>> response = itemController.getItems();

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody()
                .size());
        assertEquals("test", response.getBody()
                .get(0)
                .getName());
        assertEquals("test", response.getBody()
                .get(0)
                .getDescription());
    }

    @Test
    public void testGetItemById_whenIdExists_thenReturnSuccess() {
        Item item = new Item();
        item.setId(1L);
        item.setName("test");
        item.setDescription("test");
        item.setPrice(BigDecimal.valueOf(1.0));

        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));

        ResponseEntity<Item> response = itemController.getItemById(1L);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("test", response.getBody()
                .getName());
        assertEquals("test", response.getBody()
                .getDescription());
    }

    @Test
    public void testGetItemById_whenIdDoesNotExist_thenReturnNotFound() {
        when(itemRepository.findById(1L)).thenReturn(Optional.empty());

        ResponseEntity<Item> response = itemController.getItemById(1L);

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testGetItemByName_whenNameExists_thenReturnSuccess() {
        Item item = new Item();
        item.setName("test");
        item.setDescription("test");
        item.setPrice(BigDecimal.valueOf(1.0));

        List<Item> items = new ArrayList<>();
        items.add(item);
        when(itemRepository.findByName("test")).thenReturn(items);

        ResponseEntity<List<Item>> response = itemController.getItemsByName("test");

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody()
                .size());
        assertEquals("test", response.getBody()
                .get(0)
                .getName());
        assertEquals("test", response.getBody()
                .get(0)
                .getDescription());
    }

    @Test
    public void testGetItemByName_whenNameDoesNotExist_thenReturnNotFound() {
        when(itemRepository.findByName("test")).thenReturn(new ArrayList<>());

        ResponseEntity<List<Item>> response = itemController.getItemsByName("test");

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}
