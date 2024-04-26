package ru.mooncess.onlinestore.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    @PostMapping("/admin/create")
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

    @GetMapping()
    public ResponseEntity<?> getArticle(@RequestParam(name = "sort", required = false, defaultValue = "0") Long sort,
                                        @RequestParam(name = "category", required = false, defaultValue = "0") Long categoryId,
                                        @RequestParam(name = "name", required = false, defaultValue = "-") String reg) {
        if (!reg.equals("-")) return ResponseEntity.ok(articleService.findArticleSimName(reg));
        if (sort == 0) {
            if (categoryId == 0) {
                return ResponseEntity.ok(articleService.getAllArticle());
            }
            else {
                return ResponseEntity.ok(articleService.findArticleByCategory(categoryId));
            }
        }
        else if (sort == 1) {
            if (categoryId == 0) {
                return ResponseEntity.ok(articleService.findArticleByPriceAsc());
            }
            else {
                return ResponseEntity.ok(articleService.findArticleByCategoryAndSortByPriceAsc(categoryId));
            }
        }
        else if (sort == 2) {
            if (categoryId == 0) {
                return ResponseEntity.ok(articleService.findArticleByPriceDesc());
            }
            else {
                return ResponseEntity.ok(articleService.findArticleByCategoryAndSortByPriceDesc(categoryId));
            }
        }
        else if (sort == 3) {
            if (categoryId == 0) {
                return ResponseEntity.ok(articleService.findArticleByNameAsc());
            }
            else {
                return ResponseEntity.ok(articleService.findArticleByCategoryAndSortByNameAsc(categoryId));
            }
        }
        else if (sort == 4) {
            if (categoryId == 0) {
                return ResponseEntity.ok(articleService.findArticleByNameDesc());
            }
            else {
                return ResponseEntity.ok(articleService.findArticleByCategoryAndSortByNameDesc(categoryId));
            }
        }
        return ResponseEntity.badRequest().build();
    }

//    @PreAuthorize("hasAuthority('ADMIN')")
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
}
