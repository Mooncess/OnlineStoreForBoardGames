package ru.mooncess.onlinestore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.mooncess.onlinestore.entity.Article;
import ru.mooncess.onlinestore.entity.Category;
import ru.mooncess.onlinestore.entity.Comment;
import ru.mooncess.onlinestore.entity.User;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    @Query("SELECT c FROM Comment c WHERE c.author = :author ORDER BY c.commentDate ASC")
    List<Comment> getCommentByAuthorSortByDate(@Param("author") User author);
    @Query("SELECT c FROM Comment c WHERE c.article = :article ORDER BY c.commentDate ASC")
    List<Comment> getCommentByArticleSortByDate(@Param("article") Article article);
}
