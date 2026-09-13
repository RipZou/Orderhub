package com.orderhub.service;

import com.orderhub.domain.Role;
import com.orderhub.domain.User;
import com.orderhub.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @Mock
    UserRepository userRepository;

    @Mock
    PasswordEncoder passwordEncoder;

    @Mock
    JwtService jwtService;

    @InjectMocks
    AuthService authService;


    @Test
    void register_whenEmailExists_throwAndDoseNotSave() {

        when(userRepository.existsByEmail("a@example.com")).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> authService.register("u1", "a@example.com", "Alice", "password123"));

        verify(userRepository, never()).save(any());
    }

    @Test
    void register_whenEmailIsNew_saveUserWithHashedPasswordAndUserRole() {
        when(userRepository.existsByEmail("a@example.com")).thenReturn(false);
        when(passwordEncoder.encode("password123")).thenReturn("hashed-password");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        User saved = authService.register("u1", "a@example.com", "Alice", "password123");

        assertEquals("u1", saved.getId());
        assertEquals("a@example.com", saved.getEmail());
        assertEquals("Alice", saved.getName());
        assertEquals("hashed-password", saved.getPasswordHash());
        assertEquals(Role.USER, saved.getRole());

        verify(passwordEncoder).encode("password123");
        verify(userRepository).save(any(User.class));

    }

    @Test
    void login_whenEmailDostNotExist_throwAndNotAuthenticated() {
        when(userRepository.findByEmail("nobody@example.com")).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> authService.login("nobody@example.com", "password123"));
        verify(jwtService, never()).generateToken(any(), any());

    }

    @Test
    void login_whenPasswordIsWrong_throwAndNotAuthenticated() {
        User user = new User("u1", "a@example.com", "Alice", "hashed-password", Role.USER);
        when(userRepository.findByEmail("a@example.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrong-password", "hashed-password")).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> authService.login("a@example.com", "wrong-password"));
        verify(jwtService, never()).generateToken(any(), any());
    }

    @Test
    void login_withCorrectInfo_assignToken() {
        User user = new User("u1", "a@example.com", "Alice", "hashed-password", Role.USER);
        when(userRepository.findByEmail("a@example.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("password123", "hashed-password")).thenReturn(true);
        when(jwtService.generateToken("u1", Role.USER)).thenReturn("fake-jwt");
        
        String token = authService.login("a@example.com", "password123");
        assertEquals("fake-jwt", token);
        verify(jwtService).generateToken("u1", Role.USER);
    }


}
