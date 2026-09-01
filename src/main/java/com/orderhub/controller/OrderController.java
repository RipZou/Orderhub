package com.orderhub.controller;

import com.orderhub.domain.Order;
import com.orderhub.dto.PlaceOrderRequest;
import com.orderhub.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;


@RestController
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/orders")
    public Order placeOrder(@Valid @RequestBody PlaceOrderRequest request) {

        String buyerId = (String) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        return orderService.placeOrder(
          request.getOrderId(),
          buyerId,
          request.getProductId(),
          request.getQuantities()
        );
    }

    @GetMapping("/orders/{id}")
    public Order getOrder(@PathVariable String id) {
        return orderService.getOrder(id, currentUserId());
    }

    @PostMapping("/orders/{id}/pay")
    public Order payOrder(@PathVariable String id) {
        return orderService.payOrder(id, currentUserId());
    }

    @PostMapping("/orders/{id}/ship")
    public Order shipOrder(@PathVariable String id) {
        return orderService.shipOrder(id, currentUserId());
    }

    @PostMapping("/orders/{id}/complete")
    public Order completeOrder(@PathVariable String id) {
        return orderService.completeOrder(id, currentUserId());
    }

    @PostMapping("/orders/{id}/cancel")
    public Order cancelOrder(@PathVariable String id) {
        return orderService.cancelOrder(id, currentUserId());
    }

    private String currentUserId() {
        return (String) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
    }
}
