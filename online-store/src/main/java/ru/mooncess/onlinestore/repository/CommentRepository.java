package ru.mooncess.onlinestore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.mooncess.onlinestore.entity.Comment;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
}
