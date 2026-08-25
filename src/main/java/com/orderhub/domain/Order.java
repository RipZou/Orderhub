package com.orderhub.domain;

import jakarta.persistence.*;

import java.util.*;

@Entity
@Table(name = "orders")
public class Order {

    @Version
    private Long version;

    @Id
    private String id;
    private String buyerId;

    @ElementCollection
    @CollectionTable(
            name = "order_items",
            joinColumns = @JoinColumn(name = "order_id")
    )
    private List<OrderItem> items;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    protected Order(){}

    public Order(String id, String buyerId, List<OrderItem> items) {
        if(id == null || id.isBlank()) {
            throw new IllegalArgumentException("OrderId cannot be empty");
        }
        this.id = id;

        if(buyerId == null || buyerId.isBlank()) {
            throw new IllegalArgumentException("buyerId cannot be empty");
        }
        this.buyerId = buyerId;

        if(items == null || items.isEmpty()) {
            throw new IllegalArgumentException("items cannot be empty");
        }

        //this.items = List.copyOf(items);
        this.items = new ArrayList<>(items);


        this.status = OrderStatus.CREATED;
    }

    public String getOrderId() {
        return this.id;
    }

    public String getBuyerId() {
        return this.buyerId;
    }

    public List<OrderItem> getItems() {
        return List.copyOf(this.items);
    }

    public OrderStatus getStatus() {
        return this.status;
    }

    public double totalAmount() {
        double totalPrice = 0;

        for(OrderItem o : this.items) {
            totalPrice += o.lineTotal();
        }

        return totalPrice;
    }

    public void pay() {
        if(!this.status.canTransitionTo(OrderStatus.PAID)) {
            throw new IllegalStateException("Current status cannot transit to PAID");
        }
        this.status = OrderStatus.PAID;
    }

    public void ship() {
        if(!this.status.canTransitionTo(OrderStatus.SHIPPED)) {
            throw new IllegalStateException("Current status cannot transit to SHIPPED");
        }
        this.status = OrderStatus.SHIPPED;
    }

    public void complete() {
        if(!this.status.canTransitionTo(OrderStatus.COMPLETED)) {
            throw new IllegalStateException("Current status cannot transit to COMPLETED");
        }
        this.status = OrderStatus.COMPLETED;
    }

    public void cancel() {
        if(!this.status.canTransitionTo(OrderStatus.CANCELLED)) {
            throw new IllegalStateException("Current status cannot transit to CANCELLED");
        }
        this.status = OrderStatus.CANCELLED;
    }



}
