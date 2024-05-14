package ru.mooncess.serverjwt.controllers;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.mooncess.serverjwt.domain.JwtResponse;
import ru.mooncess.serverjwt.domain.RefreshJwtRequest;
import ru.mooncess.serverjwt.service.AuthService;
import lombok.RequiredArgsConstructor;
import ru.mooncess.serverjwt.domain.JwtRequest;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@CrossOrigin(maxAge = 3600, origins = "${client.url}", allowCredentials = "true")
@RestController
@RequestMapping("api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("login")
    public ResponseEntity<JwtResponse> login(@RequestBody JwtRequest authRequest) {
        System.out.println("Пришел запрос на вход");
        System.out.println(authRequest.getLogin() + " " + authRequest.getPassword());
        final JwtResponse token = authService.login(authRequest);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Set-Cookie", "access=" + token.getAccessToken() + "; Path=/; Max-Age=3600; HttpOnly");
        headers.add("Set-Cookie", "refresh=" + token.getRefreshToken() + "; Path=/api/auth; Max-Age=3600; HttpOnly");

        return ResponseEntity.ok()
                .headers(headers)
                .body(token);
    }

    // Добавляем новый метод logout для удаления кук
    @GetMapping("logout")
    public ResponseEntity<?> logout(HttpServletResponse response) {
        System.out.println("Запрос на выход");
        Cookie accessCookie = new Cookie("access", null);
        accessCookie.setPath("/");
        accessCookie.setMaxAge(0);
        accessCookie.setHttpOnly(true);

        Cookie refreshCookie = new Cookie("refresh", null);
        refreshCookie.setPath("/api/auth");
        refreshCookie.setMaxAge(0);
        refreshCookie.setHttpOnly(true);

        response.addCookie(accessCookie);
        response.addCookie(refreshCookie);

        return ResponseEntity.noContent().build();
    }

//    @PostMapping("token")
//    public ResponseEntity<JwtResponse> getNewAccessToken(@RequestBody RefreshJwtRequest request) {
//        final JwtResponse token = authService.getAccessToken(request.getRefreshToken());
//        return ResponseEntity.ok(token);
//    }
//
//    @PostMapping("refresh")
//    public ResponseEntity<JwtResponse> getNewRefreshToken(@RequestBody RefreshJwtRequest request) {
//        final JwtResponse token = authService.refresh(request.getRefreshToken());
//        return ResponseEntity.ok(token);
//    }

    @PostMapping("token")
    public ResponseEntity<JwtResponse> getNewAccessToken(HttpServletRequest servletRequest, HttpServletResponse servletResponse) {
        String refreshToken = getCookieValue(servletRequest, "refresh");
        System.out.println("Прилетел запрос на обновление аксес токена " + refreshToken);

        final JwtResponse token = authService.getAccessToken(refreshToken);
        System.out.println("New TOKEN: " + token.getAccessToken());
        HttpHeaders headers = new HttpHeaders();
        headers.add("Set-Cookie", "access=" + token.getAccessToken() + "; Path=/; Max-Age=3600; HttpOnly");

        return ResponseEntity.ok()
                .headers(headers)
                .body(token);
    }

    @PostMapping("refresh")
    public ResponseEntity<JwtResponse> getNewRefreshToken(HttpServletRequest servletRequest, HttpServletResponse servletResponse) {
        String refreshToken = getCookieValue(servletRequest, "refresh");
        System.out.println(refreshToken);

        final JwtResponse token = authService.refresh(refreshToken);

        HttpHeaders headers = new HttpHeaders();

        headers.add("Set-Cookie", "access=" + token.getAccessToken() + "; Path=/; Max-Age=3600; HttpOnly");
        headers.add("Set-Cookie", "refresh=" + token.getRefreshToken() + "; Path=/api/auth; Max-Age=3600; HttpOnly");

        return ResponseEntity.ok()
                .headers(headers)
                .body(token);
    }

    @GetMapping("is-admin")
    public ResponseEntity<?> isAdmin(HttpServletRequest servletRequest) {
        String accessToken = getCookieValue(servletRequest, "access");
        if (authService.isAdmin(accessToken)) {
            return ResponseEntity.status(HttpStatus.OK).build();
        }
        else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    private String getCookieValue(HttpServletRequest request, String cookieName) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(cookieName)) {
                    System.out.println(cookie.getValue());
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}
