package ru.mooncess.onlinestore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.mooncess.onlinestore.entity.Basket;

@Repository
public interface BasketRepository extends JpaRepository<Basket, Long> {

}
