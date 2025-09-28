package br.com.hyperativa.cardreader.service;

import br.com.hyperativa.cardreader.dto.RegisterRequest;
import br.com.hyperativa.cardreader.dto.RegisterResponse;
import br.com.hyperativa.cardreader.model.User;
import br.com.hyperativa.cardreader.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public RegisterResponse register(RegisterRequest registerRequest) {
        var existingUser = userRepository.findByEmail(registerRequest.getEmail());
        if (existingUser != null)
            throw new RuntimeException("Email already registered.");

        registerRequest.setPassword(encrypt(registerRequest.getPassword()));
        var user = new User(registerRequest.getEmail(), registerRequest.getPassword());
        User savedUser = userRepository.save(user);
        return new RegisterResponse(savedUser.getId(), savedUser.getEmail());
    }

    public UserDetails findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    private String encrypt(String password) {
        return new BCryptPasswordEncoder().encode(password);
    }

    public User getCurrentUser() {
        Authentication currentAuth = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = null;
        if (currentAuth.isAuthenticated()) {
            var userDetails = (UserDetails) currentAuth.getPrincipal();
            currentUser = (User) userDetails;
        }
        return currentUser;
    }
}
