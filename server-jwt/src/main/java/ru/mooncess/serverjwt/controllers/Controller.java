package ru.mooncess.serverjwt.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.mooncess.serverjwt.domain.JwtAuthentication;
import ru.mooncess.serverjwt.domain.RegistrationRequest;
import ru.mooncess.serverjwt.domain.UserForApp;
import ru.mooncess.serverjwt.service.AuthService;
import ru.mooncess.serverjwt.service.UserService;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@CrossOrigin(maxAge = 3600, origins = "${client.url}", allowCredentials = "true")
@RestController
@RequestMapping("api")
@RequiredArgsConstructor
public class Controller {
    private final AuthService authService;
    private final UserService userService;
    @Value("${app.server.url}")
    private final String appServerUrl;

    @PostMapping("/registration")
    public ResponseEntity<?> createNewUser(@RequestBody RegistrationRequest registrationRequest) {
        ResponseEntity<?> response = authService.createNewUser(registrationRequest);
        if (response.getStatusCodeValue() == 200) {
            UserForApp userForApp = new UserForApp();
            userForApp.setId(userService.findByUsername(registrationRequest.getUsername()).get().getId());
            userForApp.setUsername(registrationRequest.getUsername());
            userForApp.setFirstName(registrationRequest.getFirstName());
            userForApp.setLastName(registrationRequest.getLastName());
            userForApp.setPhoneNumber(registrationRequest.getPhoneNumber());

            WebClient webClient = WebClient.create(appServerUrl);

            // Нужна корректировка: необходимо возвращать статус ответа от сервера приложения
            // и удалять юзера из БД, если что-то пошло не так
            webClient.post()
                    .uri("/api/registration")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(Mono.just(userForApp), UserForApp.class)
                    .retrieve()
                    .bodyToMono(Integer.class)
                    .block();
        }
        return response;
    }
    @PreAuthorize("hasAuthority('USER')")
    @GetMapping("hello/user")
    public ResponseEntity<String> helloAdmin() {
        final JwtAuthentication authInfo = authService.getAuthInfo();
        return ResponseEntity.ok("Hello user " + authInfo.getPrincipal() + "!");
    }
}