package com.elearningBackend.controllers;

import com.elearningBackend.dto.*;
import com.elearningBackend.services.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;


    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        AuthService.LoginResult loginResult = authService.login(request);

        // Création du cookie HttpOnly
        ResponseCookie jwtCookie = ResponseCookie.from("jwt-token", loginResult.token())
                .httpOnly(true)
                .secure(true)       // Mettre à false seulement en dev local si pas de HTTPS
                .sameSite("Strict") // ou "Lax" - Protection CSRF
                .path("/")
                .maxAge(24 * 60 * 60) // 24 heures
                .build();

        LoginResponse authResponse = new LoginResponse("Connexion réussie", loginResult.userResponse());

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                .body(authResponse);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout() {
        // Pour se déconnecter, on envoie un cookie qui expire immédiatement
        ResponseCookie cookie = ResponseCookie.from("jwt-token", "")
                .httpOnly(true)
                .secure(true)
                .sameSite("Strict")
                .path("/")
                .maxAge(0) // Expire immédiatement
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body("Déconnexion réussie");
    }
}