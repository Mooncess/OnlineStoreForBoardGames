package ru.mooncess.onlinestore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.mooncess.onlinestore.entity.Article;
import ru.mooncess.onlinestore.entity.BasketItem;

import java.util.Optional;

@Repository
public interface BasketItemRepository extends JpaRepository<BasketItem, Long> {
    Optional<BasketItem> getByArticleAndQuantity(Article article, Integer quantity);
}
