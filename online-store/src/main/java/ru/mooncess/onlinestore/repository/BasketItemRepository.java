package ru.mooncess.onlinestore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.mooncess.onlinestore.entity.BasketItem;

@Repository
public interface BasketItemRepository extends JpaRepository<BasketItem, Long> {
}
