package com.orderhub.controller;

import com.orderhub.domain.Order;
import com.orderhub.domain.OrderItem;
import com.orderhub.dto.OrderItemResponse;
import com.orderhub.dto.OrderResponse;
import com.orderhub.dto.PlaceOrderRequest;
import com.orderhub.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/orders")
    public OrderResponse placeOrder(@Valid @RequestBody PlaceOrderRequest request) {

        String buyerId = (String) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        Order order = orderService.placeOrder(
                request.getOrderId(),
                buyerId,
                request.getProductId(),
                request.getQuantities()
        );

        return toOrderResponse(order);
    }

    @GetMapping("/orders/{id}")
    public OrderResponse getOrder(@PathVariable String id) {

        Order order = orderService.getOrder(id, currentUserId());

        return toOrderResponse(order);
    }

    @PostMapping("/orders/{id}/pay")
    public OrderResponse payOrder(@PathVariable String id) {

        Order order = orderService.payOrder(id, currentUserId());

        return toOrderResponse(order);
    }

    @PostMapping("/orders/{id}/ship")
    public OrderResponse shipOrder(@PathVariable String id) {

        Order order = orderService.shipOrder(id, currentUserId());

        return toOrderResponse(order);
    }

    @PostMapping("/orders/{id}/complete")
    public OrderResponse completeOrder(@PathVariable String id) {

        Order order = orderService.completeOrder(id, currentUserId());

        return toOrderResponse(order);
    }

    @PostMapping("/orders/{id}/cancel")
    public OrderResponse cancelOrder(@PathVariable String id) {

        Order order = orderService.cancelOrder(id, currentUserId());

        return toOrderResponse(order);
    }

    @GetMapping("/orders")
    public List<OrderResponse> getMyOrders() {
        return orderService.getMyOrders(currentUserId()).stream()
                .map(this::toOrderResponse)
                .toList();
    }

    private String currentUserId() {
        return (String) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
    }

    private OrderItemResponse toOrderItemResponse(OrderItem item) {
        return new OrderItemResponse(
                item.getProductId(),
                item.getProductName(),
                item.getUnitPrice(),
                item.getQuantity()
        );
    }

    private OrderResponse toOrderResponse(Order order) {
        List<OrderItemResponse> items = order.getItems().stream()
                .map(this::toOrderItemResponse)
                .toList();

        return new OrderResponse(
                order.getOrderId(),
                order.getBuyerId(),
                order.getStatus(),
                items
        );
    }
}
