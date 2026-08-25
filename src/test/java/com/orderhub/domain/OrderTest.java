package com.orderhub.domain;

import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class OrderTest {

    @Test
    public void testOrder_Creation() {

        List<OrderItem> list = new ArrayList<>();
        OrderItem orderlist1 = new OrderItem("001", "product1", 10, 20);
        OrderItem orderlist2 = new OrderItem("002", "product2", 5, 20);
        list.add(orderlist1);
        list.add(orderlist2);
        Order order = new Order("o001", "b001", list);

        assertEquals(OrderStatus.CREATED, order.getStatus());
    }

    @Test
    public void testOrder_GetTotalPrice() {

        List<OrderItem> list = new ArrayList<>();
        OrderItem orderlist1 = new OrderItem("001", "product1", 10, 20);
        OrderItem orderlist2 = new OrderItem("002", "product2", 5, 20);
        list.add(orderlist1);
        list.add(orderlist2);
        Order order = new Order("o001", "b001", list);

        assertEquals(300, order.totalAmount());
    }

    @Test
    public void testOrderState_Valid() {

        List<OrderItem> list = new ArrayList<>();
        OrderItem orderlist1 = new OrderItem("001", "product1", 10, 20);
        OrderItem orderlist2 = new OrderItem("002", "product2", 5, 20);
        list.add(orderlist1);
        list.add(orderlist2);
        Order order = new Order("o001", "b001", list);

        order.pay();
        assertEquals(OrderStatus.PAID, order.getStatus());
        order.ship();
        assertEquals(OrderStatus.SHIPPED, order.getStatus());
        order.complete();
        assertEquals(OrderStatus.COMPLETED, order.getStatus());
    }

    @Test
    public void testOrderState_Invalid() {

        List<OrderItem> list = new ArrayList<>();
        OrderItem orderlist1 = new OrderItem("001", "product1", 10, 20);
        OrderItem orderlist2 = new OrderItem("002", "product2", 5, 20);
        list.add(orderlist1);
        list.add(orderlist2);
        Order order = new Order("o001", "b001", list);

        assertThrows(IllegalStateException.class, () -> order.ship());
        assertThrows(IllegalStateException.class, () -> order.complete());
    }


}
