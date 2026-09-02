package com.orderhub.controller;

import com.orderhub.domain.Product;
import com.orderhub.dto.ProductResponse;
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
    public ProductResponse postProduct(@Valid @RequestBody PutProductRequest request) {
        Product product = productService.postProduct(request.getProductId(), request.getProductName(), request.getProductPrice(), request.getProductStock());

        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getPrice(),
                product.getStock()
        );
    }

    @GetMapping("/products/{id}")
    public ProductResponse getProduct(@PathVariable String id) {

        Product product = productService.getProduct(id);

        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getPrice(),
                product.getStock()
        );
    }

}
