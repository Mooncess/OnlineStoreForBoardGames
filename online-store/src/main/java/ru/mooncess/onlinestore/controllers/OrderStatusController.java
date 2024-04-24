package ru.mooncess.onlinestore.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import ru.mooncess.onlinestore.entity.OrderStatus;
import ru.mooncess.onlinestore.exception.AppError;
import ru.mooncess.onlinestore.service.OrderStatusService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("order-status")
@RequiredArgsConstructor
public class OrderStatusController {
    private final OrderStatusService orderStatusService;
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping
    public ResponseEntity<List<OrderStatus>> getAllOrderStatus() {
        return ResponseEntity.ok(orderStatusService.getAllOrderStatus());
    }
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/create")
    public ResponseEntity<?> createOrderStatus(@RequestParam String name) {
        Optional<OrderStatus> optionalOrderStatus = orderStatusService.createOrderStatus(name);
        if (optionalOrderStatus.isPresent()) {
            OrderStatus createdOrderStatus = optionalOrderStatus.get();
            return ResponseEntity.status(HttpStatus.CREATED).body(createdOrderStatus);
        } else {
            return new ResponseEntity<>(new AppError(HttpStatus.BAD_REQUEST.value(), "Incorrect creation data"), HttpStatus.BAD_REQUEST);
        }
    }
    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/update")
    public ResponseEntity<?> updateOrderStatus(@PathVariable Long id, @RequestParam String name) {
        Optional<OrderStatus> update = orderStatusService.updateOrderStatus(id, name);
        if (update.isPresent()) {
            return ResponseEntity.ok(update.get());
        }
        return new ResponseEntity<>(new AppError(HttpStatus.BAD_REQUEST.value(), "Invalid data to update"), HttpStatus.BAD_REQUEST);
    }
    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteOrderStatus(@PathVariable Long id) {
        if (orderStatusService.deleteOrderStatus(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
