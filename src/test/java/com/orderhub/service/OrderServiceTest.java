package com.orderhub.service;

import com.orderhub.repository.OrderRepository;
import com.orderhub.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @Mock
    OrderRepository orderRepo;

    @Mock
    ProductRepository productRepo;

    @InjectMocks
    OrderService orderService;

    @Test
    void orderIdDuplicate_andNotSave() {
        when(orderRepo.existsById("o1")).thenReturn(true);

        assertThrows(IllegalStateException.class, () -> orderService.placeOrder("o1", "u1", List.of("p1"), List.of(1)));

        verify(orderRepo, never()).save(any());
    }

    @Test
    void productDoesNotExist() {
        when(orderRepo.existsById("o1")).thenReturn(false);
        when(productRepo.findById("p1")).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> orderService.placeOrder("o1", "u1", List.of("p1"), List.of(1)));

        verify(orderRepo, never()).save(any());
    }
}
