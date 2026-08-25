package com.orderhub.repository;

import com.orderhub.domain.Product;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class InMemoryProductRepositoryTest {

    @Test
    public void testSaveAndGetProduct() {

        Product product1 = new Product("p001", "product1", 100, 10);
        Product product2 = new Product("p002", "product2", 200, 20);

        InMemoryProductRepository repo = new InMemoryProductRepository();

        repo.save(product1);
        repo.save(product2);

        assertEquals(product1, repo.findById(product1.getId()));
        assertEquals(product2, repo.findById(product2.getId()));


    }






}
