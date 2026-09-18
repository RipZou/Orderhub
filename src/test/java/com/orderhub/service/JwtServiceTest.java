package com.orderhub.service;

import com.orderhub.domain.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class JwtServiceTest {

    private JwtService jwtService;

    @BeforeEach
    void setUp() {
        jwtService = new JwtService();
        ReflectionTestUtils.setField(jwtService, "secret", "change-me-to-a-very-long-random-secret-at-least-32-chars");
        ReflectionTestUtils.setField(jwtService, "expirationMs", 3_600_000L);
    }

    @Test
    void tokenExtraction_OfuserIdandRole() {
        String token = jwtService.generateToken("u1", Role.USER);

        assertEquals("u1", jwtService.extractUserId(token));
        assertEquals("USER", jwtService.extractRole(token));
        assertEquals(true, jwtService.isTokenValid(token));

    }

    @Test
    void invalidToken() {
        String token = jwtService.generateToken("u1", Role.USER);
        String invalid = token.substring(1, token.length());

        assertFalse(jwtService.isTokenValid(invalid));
        assertFalse(jwtService.isTokenValid("not.a.jwt"));

    }



}
