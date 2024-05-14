package ru.mooncess.onlinestore.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.mooncess.onlinestore.entity.Category;
import ru.mooncess.onlinestore.exception.AppError;
import ru.mooncess.onlinestore.service.CategoryService;

import java.util.List;
import java.util.Optional;

@CrossOrigin(maxAge = 3600, origins = "${client.url}", allowCredentials = "true")
@RestController
@RequestMapping("category")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping
    public ResponseEntity<List<Category>> findAllCategoriesSortByName() {
        return ResponseEntity.ok(categoryService.findAllCategoriesSortByName());
    }
    @GetMapping("/admin/{id}")
    public ResponseEntity<?> getCategoryById(@PathVariable Long id) {
        Optional<Category> optionalCategory = categoryService.getCategoryById(id);
        if (optionalCategory.isPresent()) {
            Category category = optionalCategory.get();
            return ResponseEntity.ok(category);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/admin/create")
    public ResponseEntity<?> createCategory(@RequestParam String nameOfCategory) {
        Optional<Category> optionalCategory = categoryService.createCategory(nameOfCategory);
        if (optionalCategory.isPresent()) {
            Category createdCategory = optionalCategory.get();
            return ResponseEntity.status(HttpStatus.CREATED).body(createdCategory);
        }
        else {
            return new ResponseEntity<>(new AppError(HttpStatus.BAD_REQUEST.value(), "Incorrect creation data"), HttpStatus.BAD_REQUEST);
        }
    }
    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/admin/update/{id}")
    public ResponseEntity<?> updateCategory(@PathVariable Long id, @RequestParam String nameOfCategory) {
        Optional<Category> update = categoryService.updateCategory(id, nameOfCategory);
        if (update.isPresent()) {
            return ResponseEntity.ok(update.get());
        }
        return new ResponseEntity<>(new AppError(HttpStatus.BAD_REQUEST.value(), "Invalid data to update"), HttpStatus.BAD_REQUEST);
    }
    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/admin/delete/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        if (categoryService.deleteCategory(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
