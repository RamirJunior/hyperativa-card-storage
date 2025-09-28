package br.com.hyperativa.cardreader.controller;

import br.com.hyperativa.cardreader.dto.LoginRequest;
import br.com.hyperativa.cardreader.dto.LoginResponse;
import br.com.hyperativa.cardreader.dto.RegisterRequest;
import br.com.hyperativa.cardreader.dto.RegisterResponse;
import br.com.hyperativa.cardreader.model.User;
import br.com.hyperativa.cardreader.service.TokenService;
import br.com.hyperativa.cardreader.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/hyperativa/auth")
@RequiredArgsConstructor
public class AuthController {

    private final TokenService tokenService;
    private final AuthenticationManager authenticationManager;
    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        var usernamePassword = tokenService.getAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword());
        var auth = authenticationManager.authenticate(usernamePassword);
        var token = tokenService.createToken((User) auth.getPrincipal());
        return ResponseEntity.status(HttpStatus.OK).body(new LoginResponse(token));
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(@Valid @RequestBody RegisterRequest registerRequest) {
        RegisterResponse savedUser = userService.register(registerRequest);
        if (savedUser == null) return ResponseEntity.status(HttpStatus.CONFLICT).build();
        return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
    }
}
