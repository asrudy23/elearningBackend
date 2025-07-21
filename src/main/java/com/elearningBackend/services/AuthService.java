package com.elearningBackend.services;

import com.elearningBackend.dto.*;
import com.elearningBackend.mapper.MapUser;
import com.elearningBackend.models.User;
import com.elearningBackend.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;


@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final MapUser mapUser;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;


    public record LoginResult(String token, UserResponse userResponse) {}

    public LoginResult login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password())
        );
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new IllegalStateException("Erreur lors de la récupération de l'utilisateur après authentification."));

        var jwtToken = jwtService.generateToken(
                org.springframework.security.core.userdetails.User.withUsername(user.getEmail())
                        .password(user.getPassword())
                        .roles(user.getRole().name())
                        .build()
        );

        return new LoginResult(jwtToken, mapUser.mapUserToResponse(user));
    }

    // Mapper pour convertir l'entité User en DTO UserResponse

    }
