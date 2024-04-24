package ru.mooncess.onlinestore.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.mooncess.onlinestore.entity.Comment;
import ru.mooncess.onlinestore.entity.User;
import ru.mooncess.onlinestore.repository.ArticleRepository;
import ru.mooncess.onlinestore.repository.CommentRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final ArticleRepository articleRepository;

    public List<Comment> getAllComment() {
        return commentRepository.findAll();
    }

    public Optional<Comment> getCommentById(Long id) {
        return commentRepository.findById(id);
    }

    public Optional<Comment> createComment(String content, User author, long articleId) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            Comment newComment = new Comment();
            newComment.setCommentDate(LocalDateTime.now().format(formatter));
            newComment.setContent(content);
            newComment.setAuthor(author);
            newComment.setArticle(articleRepository.getById(articleId));
            return Optional.of(commentRepository.save(newComment));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public boolean deleteComment(Long id, User author) {
        Optional<Comment> optionalComment = commentRepository.findById(id);
        if (optionalComment.isPresent() && optionalComment.get().getAuthor() == author) {
            commentRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }
}
