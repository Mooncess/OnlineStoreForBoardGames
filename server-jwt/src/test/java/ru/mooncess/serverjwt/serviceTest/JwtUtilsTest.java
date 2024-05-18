package ru.mooncess.serverjwt.serviceTest;

import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.Test;
import ru.mooncess.serverjwt.domain.JwtAuthentication;
import ru.mooncess.serverjwt.domain.Role;
import ru.mooncess.serverjwt.service.JwtUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class JwtUtilsTest {

    @Test
    void testGenerate() {
        // Prepare
        Claims claims = createDummyClaims();

        // Execute
        JwtAuthentication jwtAuthentication = JwtUtils.generate(claims);

        // Verify
        assertNotNull(jwtAuthentication);
        assertEquals("testUser", jwtAuthentication.getUsername());
        assertEquals(1, jwtAuthentication.getRoles().size());
        assertEquals(Role.USER, jwtAuthentication.getRoles().iterator().next());
    }

    private Claims createDummyClaims() {
        Map<String, Object> claimsMap = new HashMap<>();
        claimsMap.put("sub", "testUser");
        claimsMap.put("role", "USER");

        Claims claims = mock(Claims.class);
        when(claims.getSubject()).thenReturn((String) claimsMap.get("sub"));
        when(claims.get("role", String.class)).thenReturn((String) claimsMap.get("role"));

        return claims;
    }
}
