package ru.mooncess.onlinestore.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.mooncess.onlinestore.dto.CommentCreateDTO;
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
//    public List<Comment> getCommentByArticleId() {
//        return commentRepository.getCommentByArticleId();
//    }

    public Optional<Comment> getCommentById(Long id) {
        return commentRepository.findById(id);
    }

    public Optional<Comment> createComment(CommentCreateDTO comment, User author) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            Comment newComment = new Comment();
            newComment.setCommentDate(LocalDateTime.now().format(formatter));
            newComment.setContent(comment.getContent());
            newComment.setAuthor(author);
            newComment.setArticle(articleRepository.getById(comment.getArticleId()));
            return Optional.of(commentRepository.save(newComment));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public Optional<Comment> updateComment(Long id, String content) {
        Optional<Comment> optionalComment = commentRepository.findById(id);
        if (optionalComment.isPresent()) {
            try {
                optionalComment.get().setContent(content);
                return Optional.of(commentRepository.save(optionalComment.get()));
            } catch (Exception e) {
                return Optional.empty();
            }
        }
        return Optional.empty();
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

    public boolean deleteCommentAdmin(Long id) {
        Optional<Comment> optionalComment = commentRepository.findById(id);
        if (optionalComment.isPresent()) {
            commentRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }
}
