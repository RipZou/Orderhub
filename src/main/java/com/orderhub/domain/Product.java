package com.orderhub.domain;


import jakarta.persistence.*;


@Entity
@Table(name = "products")
public class Product {
    @Id
    private String id;
    private String name;
    private double price;
    private int stock;

    protected Product() {
    }

    public Product(String id, String name, double price, int stock) {
        this.id = id;
        if(name == null || name.isBlank()) throw new IllegalArgumentException("Product name cannot be empty");
        this.name = name;
        if(price < 0) throw new IllegalArgumentException("Product price cannot be negative");
        this.price = price;
        if(stock < 0) throw new IllegalArgumentException("Product stock cannot be negative");
        this.stock = stock;
    }


    public String getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public void reName(String newName) {
        if(newName == null || newName.isBlank()) {
            throw new IllegalArgumentException("Product name cannot be empty");
        }

        this.name = newName;
    }

    public double getPrice() {
        return this.price;
    }

    public void setPrice(double newPrice) {
        if(newPrice < 0) throw new IllegalArgumentException("Price cannot be negative");

        this.price = newPrice;
    }

    public int getStock() {
        return this.stock;
    }

    public void increaseStock(int number) {
        if(number <= 0) throw new IllegalArgumentException("Increase number cannot <= 0");
        this.stock += number;
    }

    public void decreaseStock(int number) {
        if(number <= 0) throw new IllegalArgumentException("Decrease number cannot <= 0");
        if(this.stock - number < 0) throw new IllegalStateException("Current stock not enough");
        this.stock -= number;
    }








}
