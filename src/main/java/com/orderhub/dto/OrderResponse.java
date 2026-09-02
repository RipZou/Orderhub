package com.orderhub.dto;

import com.orderhub.domain.OrderStatus;

import java.util.List;

public class OrderResponse {

    private String orderId;
    private String buyerId;
    private OrderStatus status;
    private List<OrderItemResponse> items;

    public OrderResponse() {}

    public OrderResponse(String orderId, String buyerId, OrderStatus status, List<OrderItemResponse> items) {
        this.orderId = orderId;
        this.buyerId = buyerId;
        this.status = status;
        this.items = items;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getBuyerId() {
        return buyerId;
    }

    public void setBuyerId(String buyerId) {
        this.buyerId = buyerId;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public List<OrderItemResponse> getItems() {
        return items;
    }

    public void setItems(List<OrderItemResponse> items) {
        this.items = items;
    }

}
