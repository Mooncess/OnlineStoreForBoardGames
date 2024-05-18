package ru.mooncess.serverjwt.serviceTest;

import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.mooncess.serverjwt.config.SecurityConfig;
import ru.mooncess.serverjwt.domain.JwtRequest;
import ru.mooncess.serverjwt.domain.JwtResponse;
import ru.mooncess.serverjwt.domain.RegistrationRequest;
import ru.mooncess.serverjwt.domain.User;
import ru.mooncess.serverjwt.exception.AuthException;
import ru.mooncess.serverjwt.service.AuthService;
import ru.mooncess.serverjwt.service.JwtProvider;
import ru.mooncess.serverjwt.service.UserService;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthServiceTest {

    private AuthService authService;

    @Mock
    private UserService userService;

    private Map<String, String> refreshStorage = new HashMap<>();
    @Mock
    private JwtProvider jwtProvider;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        authService = new AuthService(userService, jwtProvider);
    }

    @Test
    void testLogin_Success() {
        JwtRequest authRequest = new JwtRequest();
        authRequest.setLogin("testUser");
        authRequest.setPassword("testPassword");
        User user = new User();
        user.setUsername("testUser");
        user.setPassword(SecurityConfig.passwordEncoder().encode("testPassword"));

        when(userService.findByUsername("testUser")).thenReturn(Optional.of(user));
        when(jwtProvider.generateAccessToken(user)).thenReturn("accessToken");
        when(jwtProvider.generateRefreshToken(user)).thenReturn("refreshToken");

        JwtResponse jwtResponse = authService.login(authRequest);

        assertNotNull(jwtResponse);
        assertEquals("accessToken", jwtResponse.getAccessToken());
        assertEquals("refreshToken", jwtResponse.getRefreshToken());
    }

    @Test
    void testLogin_UserNotFound() {
        JwtRequest authRequest = new JwtRequest();
        authRequest.setLogin("nonExistingUser");
        authRequest.setPassword("testPassword");

        when(userService.findByUsername("nonExistingUser")).thenReturn(Optional.empty());

        assertThrows(AuthException.class, () -> authService.login(authRequest));
    }

    @Test
    void testGetAccessToken_InvalidToken() {
        String invalidRefreshToken = "invalidToken";

        // Симуляция неуспешной проверки токена
        when(jwtProvider.validateRefreshToken(invalidRefreshToken)).thenReturn(false);

        JwtResponse jwtResponse = authService.getAccessToken(invalidRefreshToken);

        assertNotNull(jwtResponse);
        assertNull(jwtResponse.getAccessToken());
        assertNull(jwtResponse.getRefreshToken());
    }

    @Test
    void testCreateNewUser_UserDoesNotExist() {
        RegistrationRequest registrationRequest = new RegistrationRequest();
        registrationRequest.setUsername("newUser"); registrationRequest.setPassword("newPassword");

        when(userService.findByUsername("newUser")).thenReturn(Optional.empty());

        ResponseEntity<?> response = authService.createNewUser(registrationRequest);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testIsAdmin_ValidAccessToken() {
        String validAccessToken = "validToken";

        Claims claims = mock(Claims.class);
        when(jwtProvider.getAccessClaims(validAccessToken)).thenReturn(claims);
        when(claims.get("role", String.class)).thenReturn("ADMIN");

        assertTrue(authService.isAdmin(validAccessToken));
    }
}
