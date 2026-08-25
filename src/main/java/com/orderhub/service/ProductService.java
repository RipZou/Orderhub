package com.orderhub.service;

import com.orderhub.domain.Product;
import com.orderhub.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProductService {

    private final ProductRepository productRepo;

    public ProductService(ProductRepository productRepo) {
        this.productRepo = productRepo;
    }

    /**
     * POST Products
     * @param productId
     * @param ProductName
     * @param price
     * @param stock
     * @return
     */
    public Product postProduct(String productId, String ProductName, double price, int stock) {

        Product product = new Product(productId, ProductName, price, stock);

        productRepo.save(product);

        return product;
    }

    /**
     * GET Product
     * @param productId
     * @return
     */
    public Product getProduct(String productId) {
        Optional<Product> optional = productRepo.findById(productId);
        if (optional.isEmpty()) throw new IllegalArgumentException("Product does not exist!");
        Product product = optional.get();
        return product;
    }



}
