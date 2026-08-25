package com.orderhub.controller;

import jakarta.validation.constraints.*;

import java.util.*;

public class PlaceOrderRequest {

    @NotBlank
    private String orderId;

    @NotBlank
    private String buyerId;

    @NotEmpty
    private List<String> productId;

    @NotEmpty
    private List<Integer> quantities;


    public String getBuyerId() {
        return buyerId;
    }

    public void setBuyerId(String buyerId) {
        this.buyerId = buyerId;
    }

    public List<String> getProductId() {
        return productId;
    }

    public void setProductId(List<String> productId) {
        this.productId = productId;
    }

    public List<Integer> getQuantities() {
        return quantities;
    }

    public void setQuantities(List<Integer> quantities) {
        this.quantities = quantities;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
}
