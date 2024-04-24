package ru.mooncess.onlinestore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.mooncess.onlinestore.entity.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
}
