package ru.mooncess.onlinestore.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.mooncess.onlinestore.dto.CommentCreateDTO;
import ru.mooncess.onlinestore.entity.Comment;
import ru.mooncess.onlinestore.exception.AppError;
import ru.mooncess.onlinestore.service.AuthService;
import ru.mooncess.onlinestore.service.CommentService;
import ru.mooncess.onlinestore.service.UserService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("comment")
@RequiredArgsConstructor
public class CommentController {
    private AuthService authService;
    private UserService userService;
    private final CommentService commentService;

    @GetMapping
    public ResponseEntity<List<Comment>> getAllCategories() {
        return ResponseEntity.ok(commentService.getAllComment());
    }
    @GetMapping("/admin/{id}")
    public ResponseEntity<?> getCommentById(@PathVariable Long id) {
        Optional<Comment> optionalComment = commentService.getCommentById(id);
        if (optionalComment.isPresent()) {
            Comment comment = optionalComment.get();
            return ResponseEntity.ok(comment);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/admin/create")
    public ResponseEntity<?> createComment(@RequestBody CommentCreateDTO commentCreateDTO) {
        Optional<Comment> optionalComment = commentService.createComment(commentCreateDTO.getContent(), userService.getUserByUsername(authService.getAuthentication().getName()).get(), commentCreateDTO.getArticleId());
        if (optionalComment.isPresent()) {
            Comment createdComment = optionalComment.get();
            return ResponseEntity.status(HttpStatus.CREATED).body(createdComment);
        }
        else {
            return new ResponseEntity<>(new AppError(HttpStatus.BAD_REQUEST.value(), "Incorrect creation data"), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/admin/delete/{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long id) {
        if (commentService.deleteComment(id, userService.getUserByUsername(authService.getAuthentication().getName()).get())) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
