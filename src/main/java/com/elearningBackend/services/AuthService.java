package com.elearningBackend.services;

import com.elearningBackend.dto.*;
import com.elearningBackend.mapper.MapUser;
import com.elearningBackend.models.User;
import com.elearningBackend.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
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

    @Transactional(readOnly = true)
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
    public UserResponse getCurrentUser(Principal principal) {
       String email = principal.getName();
       User user = userRepository.findByEmail(email).orElseThrow(()->new RuntimeException("User not found "));
       return mapUser.mapUserToResponse(user);
    }
}
