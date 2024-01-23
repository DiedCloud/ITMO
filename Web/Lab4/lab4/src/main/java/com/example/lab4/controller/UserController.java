package com.example.lab4.controller;

import com.example.lab4.auth.SessionHandler;
import com.example.lab4.configuration.AppConfig;
import com.example.lab4.entity.UserEntity;
import com.example.lab4.repository.UserRepo;
import com.example.lab4.request.UserRequestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/users")
public class UserController {
    private final AuthenticationManager manager;
    private final SessionHandler handler;
    private final UserRepo repository;
    private final AppConfig config;

    @Autowired
    public UserController(final AuthenticationManager manager,
                          final SessionHandler handler,
                          final UserRepo repository,
                          final AppConfig config) {
        this.manager = manager;
        this.handler = handler;
        this.repository = repository;
        this.config = config;
    }

    @CrossOrigin
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody final UserRequestDTO dto) {
        manager.authenticate(new UsernamePasswordAuthenticationToken(
                dto.getUsername(), dto.getPassword()
        ));

        final String sessionID = handler.register(dto.getUsername());
        return ResponseEntity.ok(sessionID);
    }

    @CrossOrigin
    @DeleteMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader(HttpHeaders.AUTHORIZATION) String authentication) {
        handler.invalidate(authentication);
        return ResponseEntity.noContent().build();
    }

    @CrossOrigin
    @PostMapping("/registration")
    public ResponseEntity<String> registration(@RequestBody final UserRequestDTO dto) {
        UserEntity newUser = new UserEntity();
        newUser.setUsername(dto.getUsername());
        newUser.setPassword(config.passwordEncoder().encode(dto.getPassword()));
        newUser.setNonExpired(true);
        newUser.setNonLocked(true);
        newUser.setCredentialsNonExpired(true);
        newUser.setEnabled(true);
        try {
            repository.save(newUser);
        } catch (Throwable e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Cannot register user with that username (" + dto.getUsername() + ") " +
                            "( / highly likely it already exists).");
        }
        return login(dto);
    }
}