package com.orderhub.service;

import com.orderhub.domain.User;
import com.orderhub.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }


    @Transactional
    public User register(String id, String email, String name, String rawPassword) {

        if(userRepository.existsByEmail(email)) throw new IllegalArgumentException("User email already exist!");

        String passwordHash = passwordEncoder.encode(rawPassword);

        User user = new User(id, email, name, passwordHash);

        return userRepository.save(user);


    }


    public String login(String email, String rawPassword) {

        Optional<User> optional = userRepository.findByEmail(email);
        if(optional.isEmpty()) throw new IllegalArgumentException("Invalid email or password");
        User user = optional.get();

        if (!passwordEncoder.matches(rawPassword, user.getPasswordHash())) {
            throw new IllegalArgumentException("Invalid email or password");
        }

        return jwtService.generateToken(user.getId());


    }


}
