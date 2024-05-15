package ru.mooncess.onlinestore.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.mooncess.onlinestore.domain.JwtRequest;
import ru.mooncess.onlinestore.domain.JwtResponse;
import ru.mooncess.onlinestore.domain.RegistrationRequest;
import ru.mooncess.onlinestore.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@CrossOrigin(maxAge = 3600, origins = {"${jwt.server.url}", "${client.url}"}, allowCredentials = "true")
@RestController
@RequestMapping("api")
@RequiredArgsConstructor
public class Controller {

    private final AuthService authService;
    @Value("${jwt.server.url}")
    private String jwtServerUrl;

    @PostMapping("s-login")
    public ResponseEntity<JwtResponse> login(@RequestBody JwtRequest authRequest) {
        System.out.println("Пришел запрос на вход");

        WebClient webClient = WebClient.create(jwtServerUrl);

        // Нужна корректировка: необходимо возвращать статус ответа от сервера приложения
        // и удалять юзера из БД, если что-то пошло не так
        JwtResponse token = webClient.post()
                .uri("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(authRequest), JwtRequest.class)
                .retrieve()
                .bodyToMono(JwtResponse.class)
                .block();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Set-Cookie", "access=" + token.getAccessToken() + "; Path=/; Max-Age=3600; HttpOnly;");
        headers.add("Set-Cookie", "refresh=" + token.getRefreshToken() + "; Path=/api/auth; Max-Age=3600; HttpOnly");

        return ResponseEntity.ok()
                .headers(headers)
                .body(token);
    }

    @PreAuthorize("hasAuthority('USER')")
    @GetMapping("hello/user")
    public ResponseEntity<String> helloUser() {
        return ResponseEntity.ok("Hello user "+ authService.getAuthentication().getPrincipal() + "!");
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("hello/admin")
    public ResponseEntity<String> helloAdmin() {
        authService.getAuthentication().getName();
        return ResponseEntity.ok("Hello admin "+ authService.getAuthentication().getPrincipal() + "!");
    }

    @PostMapping("registration")
    public ResponseEntity<?> createNewUser(@RequestBody RegistrationRequest registrationRequest) {
        return authService.createNewUser(registrationRequest);
    }

}