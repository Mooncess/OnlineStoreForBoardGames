package ru.mooncess.onlinestore.serviceTest;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.Test;
import ru.mooncess.onlinestore.domain.JwtAuthentication;
import ru.mooncess.onlinestore.domain.Role;
import ru.mooncess.onlinestore.service.JwtUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class JwtUtilsTest {

    @Test
    void testGenerate() {
        Claims claims = createDummyClaims();
        JwtAuthentication jwtAuthentication = JwtUtils.generate(claims);

        assertNotNull(jwtAuthentication);
        assertEquals("testUser", jwtAuthentication.getUsername());
        assertEquals(1, jwtAuthentication.getRoles().size());
        assertEquals(Role.USER, jwtAuthentication.getRoles().iterator().next());
    }

    private Claims createDummyClaims() {
        Map<String, Object> claimsMap = new HashMap<>();
        claimsMap.put("sub", "testUser");
        claimsMap.put("role", "USER");

        return Jwts.claims(claimsMap);
    }
}
