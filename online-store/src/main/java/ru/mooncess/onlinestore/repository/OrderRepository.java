package ru.mooncess.onlinestore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.mooncess.onlinestore.entity.*;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query("SELECT o FROM Order o WHERE o.buyer= :buyer ORDER BY o.orderDate DESC")
    List<Order> findAllOrderByBuyerSortByDate(@Param("buyer") User buyer);
    @Query("SELECT o FROM Order o ORDER BY o.orderDate DESC")
    List<Order> findAllSortByDate();
    Optional<Order> getByIdAndBuyer(Long id, User buyer);
}
