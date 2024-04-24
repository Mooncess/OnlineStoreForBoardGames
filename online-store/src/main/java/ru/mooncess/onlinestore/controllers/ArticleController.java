package ru.mooncess.onlinestore.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.mooncess.onlinestore.dto.ArticleCreateDTO;
import ru.mooncess.onlinestore.entity.Article;
import ru.mooncess.onlinestore.exception.AppError;
import ru.mooncess.onlinestore.service.ArticleService;
import ru.mooncess.onlinestore.service.ImageService;

import java.util.Optional;

@RestController
@RequestMapping("article")
@RequiredArgsConstructor
public class ArticleController {
    private final ArticleService articleService;
    private final ImageService imageService;

//    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/create")
    public ResponseEntity<?> createArticle(@RequestPart ArticleCreateDTO articleCreateDTO, @RequestPart MultipartFile image) {
        String imageURN = imageService.addImage(image);
        Optional<Article> optionalArticle = articleService.createArticle(articleCreateDTO, imageURN);
        if (optionalArticle.isPresent()) {
            Article createdArticle = optionalArticle.get();
            return ResponseEntity.status(HttpStatus.CREATED).body(createdArticle);
        }
        else {
            return new ResponseEntity<>(new AppError(HttpStatus.BAD_REQUEST.value(), "Incorrect creation data"), HttpStatus.BAD_REQUEST);
        }
    }
}
