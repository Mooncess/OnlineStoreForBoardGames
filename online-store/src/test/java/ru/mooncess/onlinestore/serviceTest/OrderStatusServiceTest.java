package ru.mooncess.onlinestore.serviceTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.mooncess.onlinestore.entity.OrderStatus;
import ru.mooncess.onlinestore.repository.OrderStatusRepository;
import ru.mooncess.onlinestore.service.OrderStatusService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrderStatusServiceTest {

    @Mock
    private OrderStatusRepository orderStatusRepository;

    private OrderStatusService orderStatusService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        orderStatusService = new OrderStatusService(orderStatusRepository);
    }

    @Test
    void testGetAllOrderStatus() {
        List<OrderStatus> expectedStatusList = new ArrayList<>();
        when(orderStatusRepository.findAll()).thenReturn(expectedStatusList);

        List<OrderStatus> actualStatusList = orderStatusService.getAllOrderStatus();

        assertEquals(expectedStatusList, actualStatusList);
    }

    @Test
    void testGetOrderStatusById() {
        Long statusId = 1L;
        OrderStatus expectedStatus = new OrderStatus();
        expectedStatus.setId(statusId);
        when(orderStatusRepository.findById(statusId)).thenReturn(Optional.of(expectedStatus));

        Optional<OrderStatus> actualStatus = orderStatusService.getOrderStatusById(statusId);

        assertTrue(actualStatus.isPresent());
        assertEquals(expectedStatus, actualStatus.get());
    }

    @Test
    void testCreateOrderStatus() {
        String statusName = "New Status";
        OrderStatus createdStatus = new OrderStatus();
        createdStatus.setName(statusName);

        when(orderStatusRepository.save(any())).thenReturn(createdStatus);

        Optional<OrderStatus> newStatus = orderStatusService.createOrderStatus(statusName);

        assertTrue(newStatus.isPresent());
        assertEquals(createdStatus, newStatus.get());
    }

    @Test
    void testUpdateOrderStatus_Success() {
        Long statusId = 1L;
        String updatedStatusName = "Updated Status";
        OrderStatus existingStatus = new OrderStatus();
        existingStatus.setId(statusId);
        existingStatus.setName("Old Status");

        when(orderStatusRepository.findById(statusId)).thenReturn(Optional.of(existingStatus));
        when(orderStatusRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        Optional<OrderStatus> updatedStatus = orderStatusService.updateOrderStatus(statusId, updatedStatusName);

        assertTrue(updatedStatus.isPresent());
        assertEquals(updatedStatusName, updatedStatus.get().getName());
        assertEquals(statusId, updatedStatus.get().getId());
    }

    @Test
    void testUpdateOrderStatus_Failure() {
        Long statusId = 1L;
        String updatedStatusName = "Updated Status";

        when(orderStatusRepository.findById(statusId)).thenReturn(Optional.empty());

        Optional<OrderStatus> updatedStatus = orderStatusService.updateOrderStatus(statusId, updatedStatusName);

        assertTrue(updatedStatus.isEmpty());
    }

    @Test
    void testDeleteOrderStatus() {
        Long statusId = 1L;
        OrderStatus existingStatus = new OrderStatus();
        existingStatus.setId(statusId);

        when(orderStatusRepository.findById(statusId)).thenReturn(Optional.of(existingStatus));

        assertTrue(orderStatusService.deleteOrderStatus(statusId));

        verify(orderStatusRepository, times(1)).deleteById(statusId);
    }

    @Test
    void testDeleteOrderStatus_NotFound() {
        Long statusId = 1L;

        when(orderStatusRepository.findById(statusId)).thenReturn(Optional.empty());

        assertFalse(orderStatusService.deleteOrderStatus(statusId));
    }
}