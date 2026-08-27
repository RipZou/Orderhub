package com.orderhub.service;

import com.orderhub.domain.User;
import com.orderhub.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }


    @Transactional
    public User register(String id, String email, String name, String rawPassword) {

        if(userRepository.existsByEmail(email)) throw new IllegalArgumentException("User email already exist!");

        String passwordHash = passwordEncoder.encode(rawPassword);

        User user = new User(id, email, name, passwordHash);

        return userRepository.save(user);


    }

}
