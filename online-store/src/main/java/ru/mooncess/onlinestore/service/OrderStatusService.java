package ru.mooncess.onlinestore.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.mooncess.onlinestore.entity.Category;
import ru.mooncess.onlinestore.entity.OrderStatus;
import ru.mooncess.onlinestore.repository.OrderStatusRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderStatusService {
    private final OrderStatusRepository orderStatusRepository;

    public List<OrderStatus> getAllOrderStatus() {
        return orderStatusRepository.findAll();
    }
    public Optional<OrderStatus> getOrderStatusById(Long id) {
        return orderStatusRepository.findById(id);
    }

    public Optional<OrderStatus> createOrderStatus(String name) {
        OrderStatus orderStatus = new OrderStatus();
        orderStatus.setName(name);
        return Optional.of(orderStatusRepository.save(orderStatus));
    }

    public Optional<OrderStatus> updateOrderStatus(Long id, String name) {
        Optional<OrderStatus> optionalOrderStatus = orderStatusRepository.findById(id);
        if (optionalOrderStatus.isPresent()) {
            OrderStatus update = new OrderStatus();
            update.setName(name);
            update.setId(id);
            return Optional.of(orderStatusRepository.save(update));
        }
        return Optional.empty();
    }

    public boolean deleteOrderStatus(Long id) {
        Optional<OrderStatus> optionalOrderStatus = orderStatusRepository.findById(id);
        if (optionalOrderStatus.isPresent()) {
            orderStatusRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }
}
