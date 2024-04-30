package ru.mooncess.onlinestore.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.mooncess.onlinestore.exception.AppError;
import ru.mooncess.onlinestore.service.ArticleService;
import ru.mooncess.onlinestore.service.AuthService;
import ru.mooncess.onlinestore.service.UserService;

@RestController
@RequestMapping("action")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final AuthService authService;
    private final ArticleService articleService;

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/get-all-users")
    public ResponseEntity<?> getAllUser() {
        return ResponseEntity.ok(userService.findAllUsers());
    }
    @PreAuthorize("hasAuthority('USER')")
    @GetMapping("/get-user-basket")
    public ResponseEntity<?> getUserBasket() {
        return ResponseEntity.ok(userService.getUserBasket(userService.getUserByUsername(authService.getAuthentication().getName()).get()));
    }

    @PreAuthorize("hasAuthority('USER')")
    @GetMapping("/get-user-wishlist")
    public ResponseEntity<?> getUserWishlist() {
        return ResponseEntity.ok(userService.getUserWishlist(userService.getUserByUsername(authService.getAuthentication().getName()).get()));
    }

    @PreAuthorize("hasAuthority('USER')")
    @PostMapping("/add-to-basket")
    public ResponseEntity<?> addToBasket(@RequestParam Long articleId) {
        if(articleService.addToBasket(articleId, userService.getUserByUsername(authService.getAuthentication().getName()).get())) {
            return ResponseEntity.status(HttpStatus.OK).build();
        }
        else {
            return new ResponseEntity<>(new AppError(HttpStatus.BAD_REQUEST.value(), "Incorrect data"), HttpStatus.BAD_REQUEST);
        }
    }
    @PreAuthorize("hasAuthority('USER')")
    @PutMapping("/increase-count-of-basket-item")
    public ResponseEntity<?> increaseCountOfBasketItem(@RequestParam Long articleId) {
        if(articleService.increaseCountOfBasketItem(articleId, userService.getUserByUsername(authService.getAuthentication().getName()).get())) {
            return ResponseEntity.status(HttpStatus.OK).build();
        }
        else {
            return new ResponseEntity<>(new AppError(HttpStatus.BAD_REQUEST.value(), "Incorrect data"), HttpStatus.BAD_REQUEST);
        }
    }
    @PreAuthorize("hasAuthority('USER')")
    @PutMapping("/decrease-count-of-basket-item")
    public ResponseEntity<?> decreaseCountOfBasketItem(@RequestParam Long articleId) {
        if(articleService.decreaseCountOfBasketItem(articleId, userService.getUserByUsername(authService.getAuthentication().getName()).get())) {
            return ResponseEntity.status(HttpStatus.OK).build();
        }
        else {
            return new ResponseEntity<>(new AppError(HttpStatus.BAD_REQUEST.value(), "Incorrect data"), HttpStatus.BAD_REQUEST);
        }
    }
    @PreAuthorize("hasAuthority('USER')")
    @DeleteMapping("/delete-from-basket")
    public ResponseEntity<?> deleteArticleFromBasket(@RequestParam Long articleId) {
        if(articleService.deleteArticleFromBasket(articleId, userService.getUserByUsername(authService.getAuthentication().getName()).get())) {
            return ResponseEntity.status(HttpStatus.OK).build();
        }
        else {
            return new ResponseEntity<>(new AppError(HttpStatus.BAD_REQUEST.value(), "Incorrect data"), HttpStatus.BAD_REQUEST);
        }
    }
    @PreAuthorize("hasAuthority('USER')")
    @PostMapping("/add-to-wishlist")
    public ResponseEntity<?> addToWishList(@RequestParam Long articleId) {
        if(articleService.addToWishlist(articleId, userService.getUserByUsername(authService.getAuthentication().getName()).get())) {
            return ResponseEntity.status(HttpStatus.OK).build();
        }
        else {
            return new ResponseEntity<>(new AppError(HttpStatus.BAD_REQUEST.value(), "Incorrect data"), HttpStatus.BAD_REQUEST);
        }
    }
    @PreAuthorize("hasAuthority('USER')")
    @DeleteMapping("/delete-from-wishlist")
    public ResponseEntity<?> deleteArticleFromWishlist(@RequestParam Long articleId) {
        if(articleService.deleteArticleFromWishlist(articleId, userService.getUserByUsername(authService.getAuthentication().getName()).get())) {
            return ResponseEntity.status(HttpStatus.OK).build();
        }
        else {
            return new ResponseEntity<>(new AppError(HttpStatus.BAD_REQUEST.value(), "Incorrect data"), HttpStatus.BAD_REQUEST);
        }
    }
    @PreAuthorize("hasAuthority('USER')")
    @PutMapping("/change-phone-number")
    public ResponseEntity<?> changePhoneNumber(@RequestParam String phone) {
        if(userService.changePhoneNumber(phone, userService.getUserByUsername(authService.getAuthentication().getName()).get())) {
            return ResponseEntity.status(HttpStatus.OK).build();
        }
        else {
            return new ResponseEntity<>(new AppError(HttpStatus.BAD_REQUEST.value(), "Incorrect data"), HttpStatus.BAD_REQUEST);
        }
    }
}
