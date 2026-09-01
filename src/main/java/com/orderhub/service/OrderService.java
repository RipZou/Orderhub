package com.orderhub.service;

import com.orderhub.repository.*;
import com.orderhub.domain.*;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
public class OrderService {

    private final OrderRepository OrderRepo;
    private final ProductRepository ProductRepo;

    public OrderService(OrderRepository OrderRepo, ProductRepository ProductRepo) {
        this.OrderRepo = OrderRepo;
        this.ProductRepo = ProductRepo;
    }


    /**
     * PLACE Order
     * @param orderId
     * @param buyerId
     * @param productId
     * @param quantity
     * @return
     */
    @Transactional
    public Order placeOrder(String orderId, String buyerId, List<String> productId, List<Integer> quantity) {

        if(OrderRepo.existsById(orderId)) {
            throw new IllegalStateException("Order already exist");
        }

        if(productId.size() != quantity.size()) throw new IllegalArgumentException("Product type and quantity type don't match");

        List<OrderItem> orderList = new ArrayList<>();

        for(int i = 0; i < productId.size(); i ++) {

            /**
             * Product check
             */
            Optional<Product> optional = ProductRepo.findById(productId.get(i));
            if(optional.isEmpty()) throw new IllegalArgumentException("product dose not exist!");
            Product curProduct = optional.get();

            /**
             * Stock decrease & Product save
             */
            curProduct.decreaseStock(quantity.get(i));
            ProductRepo.save(curProduct);

            OrderItem orderItem = new OrderItem(curProduct.getId(), curProduct.getName(), curProduct.getPrice(), quantity.get(i));
            orderList.add(orderItem);
        }

        /**
         * Create and Save order
         */
        Order curOrder = new Order(orderId, buyerId, orderList);
        OrderRepo.save(curOrder);

        // validation
        // throw new IllegalStateException("test rollback");

        return curOrder;
    }

    /**
     * GET Order
     * @param OrderId
     * @return
     */
    public Order getOrder(String OrderId, String currentUserId) {
        /**
         * Order order = OrderRepo.findById(orderId)
         *         .orElseThrow(() -> new IllegalArgumentException("Order does not exist!"));
         */

        Optional<Order> optional = OrderRepo.findById(OrderId);
        if(optional.isEmpty()) throw new IllegalArgumentException("Order does not exist!");
        Order order = optional.get();

        assertOrderOwner(order, currentUserId);

        return order;
    }


    /**
     * PAY Order
     * @param OrderId
     * @return
     */
    @Transactional
    public Order payOrder(String OrderId, String currentUserId) {
        Order order = getOrder(OrderId, currentUserId);
        order.pay();
        OrderRepo.save(order);
        return order;
    }


    /**
     * SHIP Order
     * @param OrderId
     * @return
     */
    @Transactional
    public Order shipOrder(String OrderId, String currentUserId) {
        Order order = getOrder(OrderId, currentUserId);
        order.ship();
        OrderRepo.save(order);
        return order;
    }

    /**
     * COMPLETE Order
     * @param OrderId
     * @return
     */
    @Transactional
    public Order completeOrder(String OrderId, String currentUserId) {
        Order order = getOrder(OrderId, currentUserId);
        order.complete();
        OrderRepo.save(order);
        return order;
    }

    /**
     * CANCEL Order
     * @param OrderId
     * @return
     */
    @Transactional
    public Order cancelOrder(String OrderId, String currentUserId) {
        Order order = getOrder(OrderId, currentUserId);
        order.cancel();
        OrderRepo.save(order);
        return order;
    }

    public List<Order> getMyOrders(String currentUserId) {
        return OrderRepo.findByBuyerId(currentUserId);
    }


    private void assertOrderOwner(Order order, String currentUserId) {
        if(!order.getBuyerId().equals(currentUserId)) {
            throw new org.springframework.security.access.AccessDeniedException("Access denied");
        }
    }






}
