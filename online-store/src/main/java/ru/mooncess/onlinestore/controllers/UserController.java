package ru.mooncess.onlinestore.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.mooncess.onlinestore.entity.Order;
import ru.mooncess.onlinestore.exception.AppError;
import ru.mooncess.onlinestore.service.ArticleService;
import ru.mooncess.onlinestore.service.AuthService;
import ru.mooncess.onlinestore.service.OrderService;
import ru.mooncess.onlinestore.service.UserService;

import java.util.List;
import java.util.Optional;

@CrossOrigin(maxAge = 3600, origins = "http://localhost:3000", allowCredentials = "true")
@RestController
@RequestMapping("action")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final AuthService authService;
    private final OrderService orderService;
    private final ArticleService articleService;

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/get-all-users")
    public ResponseEntity<?> getAllUser() {
        return ResponseEntity.ok(userService.findAllUsers());
    }
    @PreAuthorize("hasAuthority('USER')")
    @GetMapping("/profile")
    public ResponseEntity<?> getUserInfo() {
        return ResponseEntity.ok(userService.getUserByUsername(authService.getAuthentication().getName()).get());
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

    @PreAuthorize("hasAuthority('USER')")
    @PostMapping("/create-order")
    public ResponseEntity<?> createOrder(@RequestParam String address) {
        if (orderService.createOrder(address, userService.getUserByUsername(authService.getAuthentication().getName()).get()).isEmpty()){
            return new ResponseEntity<>(new AppError(HttpStatus.BAD_REQUEST.value(), "Incorrect data"), HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasAuthority('USER')")
    @GetMapping("/get-user-order")
    public ResponseEntity<?> getUserOrder() {
        return ResponseEntity.ok(orderService.getUserOrder(userService.getUserByUsername(authService.getAuthentication().getName()).get()));
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/get-all-order")
    public ResponseEntity<?> getAllOrder() {
        return ResponseEntity.ok(orderService.getAllOrder());
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/admin/order-by-id/{id}")
    public ResponseEntity<?> getOrderByIdForAdmin(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.getOrderByIdForAdmin(id));
    }

    @PreAuthorize("hasAuthority('USER')")
    @GetMapping("/order/{id}")
    public ResponseEntity<?> getOrderById(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.getOrderByIdAndUserId(id, userService.getUserByUsername(authService.getAuthentication().getName()).get()));
    }
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/admin/order-by-username/{username}")
    public ResponseEntity<?> getOrderByUserForAdmin(@PathVariable String username) {
        Optional<List<Order>> list = orderService.getOrderByUserForAdmin(username);
        if (list.isPresent()) {
            return ResponseEntity.status(HttpStatus.OK).body(list.get());
        }
        else {
            return new ResponseEntity<>(new AppError(HttpStatus.BAD_REQUEST.value(), "Пользователь не найден"), HttpStatus.BAD_REQUEST);
        }
    }
    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/admin/order/update-status")
    public ResponseEntity<?> updateOrder(@RequestParam Long order, @RequestParam Long status) {
        Optional<Order> update = orderService.updateOrder(order, status);
        if (update.isPresent()) {
            return ResponseEntity.ok(update.get());
        }
        return new ResponseEntity<>(new AppError(HttpStatus.BAD_REQUEST.value(), "Invalid data to update"), HttpStatus.BAD_REQUEST);
    }
    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/admin/order/delete/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        if (orderService.deleteOrder(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
