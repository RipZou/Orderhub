package com.orderhub.controller;

import com.orderhub.domain.Product;
import com.orderhub.dto.PutProductRequest;
import com.orderhub.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("/products")
    public Product postProduct(@Valid @RequestBody PutProductRequest request) {
        return productService.postProduct(request.getProductId(), request.getProductName(), request.getProductPrice(), request.getProductStock());
    }

    @GetMapping("/products/{id}")
    public Product getProduct(@PathVariable String id) {
        return productService.getProduct(id);
    }

}
