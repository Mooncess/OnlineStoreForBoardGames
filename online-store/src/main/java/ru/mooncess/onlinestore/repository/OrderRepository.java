package ru.mooncess.onlinestore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.mooncess.onlinestore.entity.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
}
