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

@CrossOrigin(maxAge = 3600, origins = "http://localhost:3000", allowCredentials = "true")
@RestController
@RequestMapping("comment")
@RequiredArgsConstructor
public class CommentController {
    private final UserService userService;
    private final AuthService authService;
    private final CommentService commentService;

    @GetMapping("/all")
    public ResponseEntity<List<Comment>> getAllComment() {
        return ResponseEntity.ok(commentService.getAllComment());
    }
    @PreAuthorize("hasAuthority('USER')")
    @GetMapping("user-comments")
    public ResponseEntity<List<Comment>> getCommentByAuthorSortByDate() {
        return ResponseEntity.ok(commentService.getCommentByAuthorSortByDate(userService.getUserByUsername(authService.getAuthentication().getName()).get()));
    }
    @GetMapping("article-comments/{id}")
    public ResponseEntity<List<Comment>> getCommentByArticleSortByDate(@PathVariable Long id) {
        Optional<List<Comment>> list = commentService.getCommentByArticleSortByDate(id);
        return list.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
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

    @PreAuthorize("hasAuthority('USER')")
    @PostMapping("/create")
    public ResponseEntity<?> createComment(@RequestBody CommentCreateDTO commentCreateDTO) {
        Optional<Comment> optionalComment = commentService.createComment(commentCreateDTO, userService.getUserByUsername(authService.getAuthentication().getName()).get());
        if (optionalComment.isPresent()) {
            Comment createdComment = optionalComment.get();
            return ResponseEntity.status(HttpStatus.CREATED).build();
        }
        else {
            return new ResponseEntity<>(new AppError(HttpStatus.BAD_REQUEST.value(), "Incorrect creation data"), HttpStatus.BAD_REQUEST);
        }
    }

    @PreAuthorize("hasAuthority('USER')")
    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateComment(@PathVariable Long id, @RequestParam String content) {
        Optional<Comment> update = commentService.updateComment(id, content, userService.getUserByUsername(authService.getAuthentication().getName()).get());
        if (update.isPresent()) {
            return ResponseEntity.ok(update.get());
        }
        return new ResponseEntity<>(new AppError(HttpStatus.BAD_REQUEST.value(), "Invalid data to update"), HttpStatus.BAD_REQUEST);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/admin/delete/{id}")
    public ResponseEntity<Void> deleteCommentAdmin(@PathVariable Long id) {
        if (commentService.deleteCommentAdmin(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
    @PreAuthorize("hasAuthority('USER')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long id) {
        if (commentService.deleteComment(id, userService.getUserByUsername(authService.getAuthentication().getName()).get())) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
