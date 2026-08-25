package com.orderhub.domain;

import jakarta.persistence.Embeddable;

@Embeddable
public class OrderItem {
    private String productId;
    private String productName;
    private double unitPrice;
    private int quantity;

    protected OrderItem() {
    }

    public OrderItem(String id, String name, double price, int quantity) {
        if(id == null || id.isBlank()) {
            throw new IllegalArgumentException("ProductId cannot be empty");
        }
        this.productId = id;

        if(name == null || name.isBlank()) {
            throw new IllegalArgumentException("ProductName cannot be empty");
        }
        this.productName = name;

        if(price < 0) {
            throw new IllegalArgumentException("ProductPrice cannot be negative");
        }
        this.unitPrice = price;

        if(quantity <= 0) {
            throw new IllegalArgumentException("Quantity cannot <= 0");
        }
        this.quantity = quantity;
    }

    public String getProductId() {
        return this.productId;
    }

    public String getProductName() {
        return this.productName;
    }

    public double getUnitPrice() {
        return this.unitPrice;
    }

    public int getQuantity() {
        return this.quantity;
    }

    public double lineTotal() {
        return this.unitPrice * this.quantity;
    }

}
