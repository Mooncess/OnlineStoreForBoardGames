package ru.mooncess.onlinestore.serviceTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import ru.mooncess.onlinestore.dto.CommentCreateDTO;
import ru.mooncess.onlinestore.entity.Article;
import ru.mooncess.onlinestore.entity.Comment;
import ru.mooncess.onlinestore.entity.User;
import ru.mooncess.onlinestore.repository.ArticleRepository;
import ru.mooncess.onlinestore.repository.CommentRepository;
import ru.mooncess.onlinestore.service.ArticleService;
import ru.mooncess.onlinestore.service.CommentService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class CommentServiceTest {
    @Mock
    private CommentRepository commentRepository;
    @Mock
    private ArticleRepository articleRepository;
    @Mock
    private ArticleService articleService;
    private CommentService commentService;
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        commentService = new CommentService(commentRepository, articleRepository, articleService);;
    }

    @Test
    void testCreateComment() {
        CommentCreateDTO commentCreateDTO = new CommentCreateDTO();
        Comment comment = new Comment();
        User author = new User();
        Article article = new Article();

        when(articleRepository.getById(commentCreateDTO.getArticleId())).thenReturn(article);
        when(commentRepository.save(Mockito.any(Comment.class))).thenReturn(comment);

        Optional<Comment> createdComment = commentService.createComment(commentCreateDTO, author);

        assertTrue(createdComment.isPresent());
    }

    @Test
    void testGetAllComment() {
        List<Comment> expectedComments = new ArrayList<>();
        when(commentRepository.findAll()).thenReturn(expectedComments);

        List<Comment> actualComments = commentService.getAllComment();

        assertEquals(expectedComments, actualComments);
    }

    @Test
    void testGetCommentByAuthorSortByDate() {
        User user = new User(); // Provide necessary user details
        List<Comment> expectedComments = new ArrayList<>(); // Provide expected comments

        when(commentRepository.getCommentByAuthorSortByDate(user)).thenReturn(expectedComments);

        List<Comment> actualComments = commentService.getCommentByAuthorSortByDate(user);

        assertEquals(expectedComments, actualComments);
    }

    @Test
    void testGetCommentByArticleSortByDate() {
        Long articleId = 1L;
        Article article = new Article(); // Provide necessary article details

        when(articleService.getArticleById(articleId)).thenReturn(Optional.of(article));

        List<Comment> expectedComments = new ArrayList<>(); // Provide expected comments

        when(commentRepository.getCommentByArticleSortByDate(article)).thenReturn(expectedComments);

        Optional<List<Comment>> actualComments = commentService.getCommentByArticleSortByDate(articleId);

        assertTrue(actualComments.isPresent());
        assertEquals(expectedComments, actualComments.get());
    }

    @Test
    void testGetCommentById() {
        Long commentId = 1L;
        Comment expectedComment = new Comment(); // Provide expected comment

        when(commentRepository.findById(commentId)).thenReturn(Optional.of(expectedComment));

        Optional<Comment> actualComment = commentService.getCommentById(commentId);

        assertTrue(actualComment.isPresent());
        assertEquals(expectedComment, actualComment.get());
    }



    @Test
    void testUpdateComment() {
        Long commentId = 1L;
        String newContent = "Updated Content";
        User author = new User(); // Provide necessary user details

        Comment existingComment = new Comment(); // Provide existing comment details
        existingComment.setAuthor(author);

        Comment _updatedComment = new Comment(); // Provide existing comment details
        _updatedComment.setAuthor(author);
        _updatedComment.setContent(newContent);

        when(commentRepository.findById(commentId)).thenReturn(Optional.of(existingComment));
        when(commentRepository.save(existingComment)).thenReturn(_updatedComment);

        Optional<Comment> updatedComment = commentService.updateComment(commentId, newContent, author);

        // Assert that the comment is updated successfully
        assertTrue(updatedComment.isPresent());
        assertEquals(newContent, updatedComment.get().getContent());
    }

    @Test
    void testDeleteComment() {
        Long commentId = 1L;
        User author = new User(); // Provide necessary user details

        Comment commentToDelete = new Comment(); // Provide comment details to delete
        commentToDelete.setAuthor(author);

        when(commentRepository.findById(commentId)).thenReturn(Optional.of(commentToDelete));

        boolean isDeleted = commentService.deleteComment(commentId, author);

        // Assert that the comment is deleted successfully
        assertTrue(isDeleted);
    }

    @Test
    void testDeleteCommentAdmin() {
        Long commentId = 1L;
        Comment commentToDelete = new Comment(); // Provide comment details to delete

        when(commentRepository.findById(commentId)).thenReturn(Optional.of(commentToDelete));

        boolean isDeleted = commentService.deleteCommentAdmin(commentId);

        // Assert that the comment is deleted by admin successfully
        assertTrue(isDeleted);
    }
}
