package com.orderhub.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ProductTest {

    @Test
    void increaseStock_WithValidNumber() {
        Product product = new Product("001", "product1", 521, 100);
        product.increaseStock(100);
        assertEquals(200, product.getStock());
    }

    @Test
    void increaseStock_WithInvalidNumber() {
        Product product = new Product("001", "product1", 521, 100);
        assertThrows(IllegalArgumentException.class, () -> product.increaseStock(-1));
    }

    @Test
    void decreaseStock_WithValidNumber() {
        Product product = new Product("001", "product1", 521, 100);
        product.decreaseStock(99);
        assertEquals(1, product.getStock());
    }

    @Test
    void decreaseStock_WithInvalidNumebr() {
        Product product = new Product("001", "product1", 521, 100);
        assertThrows(IllegalStateException.class, () -> product.decreaseStock(101));
    }



}
