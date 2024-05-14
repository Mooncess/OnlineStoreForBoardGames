package ru.mooncess.onlinestore.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.mooncess.onlinestore.dto.ArticleCreateDTO;
import ru.mooncess.onlinestore.entity.Article;
import ru.mooncess.onlinestore.entity.Category;
import ru.mooncess.onlinestore.exception.AppError;
import ru.mooncess.onlinestore.service.ArticleService;
import ru.mooncess.onlinestore.service.ImageService;

import java.io.IOException;
import java.util.Optional;

@CrossOrigin(maxAge = 3600, origins = "${client.url}", allowCredentials = "true")
@RestController
@RequestMapping("article")
@RequiredArgsConstructor
public class ArticleController {
    private final ArticleService articleService;
    private final ImageService imageService;

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/admin/create")
    public ResponseEntity<?> createArticle(@RequestPart ArticleCreateDTO articleCreateDTO, @RequestPart(required = false) MultipartFile image) {
        System.out.println(articleCreateDTO.getName());
        Optional<Article> optionalArticle = articleService.createArticle(articleCreateDTO, image);
        if (optionalArticle.isPresent()) {
            Article createdArticle = optionalArticle.get();
            return ResponseEntity.status(HttpStatus.CREATED).body(createdArticle);
        }
        else {
            return new ResponseEntity<>(new AppError(HttpStatus.BAD_REQUEST.value(), "Incorrect creation data"), HttpStatus.BAD_REQUEST);
        }
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/admin/update/{id}")
    public ResponseEntity<?> updateCategory(@PathVariable Long id,
                                            @RequestPart ArticleCreateDTO articleUpdateDTO,
                                            @RequestPart(required = false) MultipartFile image) {
        Optional<Article> update = articleService.updateArticle(id, articleUpdateDTO, image);
        if (update.isPresent()) {
            return ResponseEntity.ok(update.get());
        }
        return new ResponseEntity<>(new AppError(HttpStatus.BAD_REQUEST.value(), "Invalid data to update"), HttpStatus.BAD_REQUEST);
    }
    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/admin/delete/{id}")
    public ResponseEntity<Void> deleteArticle(@PathVariable Long id) {
        if (articleService.deleteArticle(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getArticleById(@PathVariable Long id) {
        Optional<Article> optionalArticle = articleService.getArticleById(id);
        if (optionalArticle.isPresent()) {
            Article article = optionalArticle.get();
            return ResponseEntity.ok(article);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping()
    public ResponseEntity<?> getArticle(@RequestParam(name = "sort", required = false, defaultValue = "0") Long sort,
                                        @RequestParam(name = "category", required = false) Long categoryId,
                                        @RequestParam(name = "name", required = false) String reg) {
        if (sort == 0 && categoryId == null && reg == null) return ResponseEntity.ok(articleService.getAllArticle());
        else if (sort == 1 && categoryId == null && reg == null) return ResponseEntity.ok(articleService.findArticleByPriceAsc());
        else if (sort == 2 && categoryId == null && reg == null) return ResponseEntity.ok(articleService.findArticleByPriceDesc());
        else if (sort == 3 && categoryId == null && reg == null) return ResponseEntity.ok(articleService.findArticleByNameAsc());
        else if (sort == 4 && categoryId == null && reg == null) return ResponseEntity.ok(articleService.findArticleByNameDesc());
        else if (sort == 0 && categoryId != null && reg == null) return ResponseEntity.ok(articleService.findArticleByCategory(categoryId));
        else if (sort == 1 && categoryId != null && reg == null) return ResponseEntity.ok(articleService.findArticleByCategoryAndSortByPriceAsc(categoryId));
        else if (sort == 2 && categoryId != null && reg == null) return ResponseEntity.ok(articleService.findArticleByCategoryAndSortByPriceDesc(categoryId));
        else if (sort == 3 && categoryId != null && reg == null) return ResponseEntity.ok(articleService.findArticleByCategoryAndSortByNameAsc(categoryId));
        else if (sort == 4 && categoryId != null && reg == null) return ResponseEntity.ok(articleService.findArticleByCategoryAndSortByNameDesc(categoryId));
        else if (sort == 0 && categoryId == null && reg != null) return ResponseEntity.ok(articleService.findArticleBySimName(reg));
        else if (sort == 1 && categoryId == null && reg != null) return ResponseEntity.ok(articleService.findArticleBySimNameAndSortByPriceAsc(reg));
        else if (sort == 2 && categoryId == null && reg != null) return ResponseEntity.ok(articleService.findArticleBySimNameAndSortByPriceDesc(reg));
        else if (sort == 3 && categoryId == null && reg != null) return ResponseEntity.ok(articleService.findArticleBySimNameAndSortByNameAsc(reg));
        else if (sort == 4 && categoryId == null && reg != null) return ResponseEntity.ok(articleService.findArticleBySimNameAndSortByNameDesc(reg));
        else if (sort == 0 && categoryId != null && reg != null) return ResponseEntity.ok(articleService.findArticleByCategoryAndSimName(categoryId, reg));
        else if (sort == 1 && categoryId != null && reg != null) return ResponseEntity.ok(articleService.findArticleByCategoryAndSimNameAndSortByPriceAsc(categoryId, reg));
        else if (sort == 2 && categoryId != null && reg != null) return ResponseEntity.ok(articleService.findArticleByCategoryAndSimNameAndSortByPriceDesc(categoryId, reg));
        else if (sort == 3 && categoryId != null && reg != null) return ResponseEntity.ok(articleService.findArticleByCategoryAndSimNameAndSortByNameAsc(categoryId, reg));
        else if (sort == 4 && categoryId != null && reg != null) return ResponseEntity.ok(articleService.findArticleByCategoryAndSimNameAndSortByNameDesc(categoryId, reg));
        return ResponseEntity.badRequest().build();
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/admin")
    public ResponseEntity<?> getArticleByReserves(@RequestParam(name = "sort", required = false, defaultValue = "0") Long sort) {
        if (sort == 0) {
            return ResponseEntity.ok(articleService.getAllArticle());
        }
        else if (sort == 1) {
            return ResponseEntity.ok(articleService.findArticleByReservesAsc());
        }
        else if (sort == 2) {
            return ResponseEntity.ok(articleService.findArticleByReservesDesc());
        }
        return ResponseEntity.badRequest().build();
    }

    @GetMapping("/image/{articleId}")
    public ResponseEntity<byte[]> getImageForArticle(@PathVariable Long articleId) {
        try {
            byte[] imageBytes = imageService.getImageByURN(articleService.getArticleById(articleId).get().getImageURN());
            return ResponseEntity.ok().body(imageBytes);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); // Можно также возвращать подходящий ответ в случае ошибки
        }
    }
}
