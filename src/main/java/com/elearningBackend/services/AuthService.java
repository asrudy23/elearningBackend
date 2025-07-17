package com.elearningBackend.services;

import com.elearningBackend.configuration.JwtUtils;
import com.elearningBackend.dto.JwtResponse;
import com.elearningBackend.dto.LoginRequest;
import com.elearningBackend.exception.InvalidTokenException;
import com.elearningBackend.exception.TokenExpiredException;
import com.elearningBackend.exception.UnauthorizedRoleException;
import com.elearningBackend.models.PasswordResetToken;
import com.elearningBackend.models.User;
import com.elearningBackend.repositories.PasswordResetTokenRepository;
import com.elearningBackend.repositories.UserRepository;
import com.elearningBackend.security.UserDetailsImpl;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final TokenBlacklistService tokenBlacklistService;
    private final JavaMailSender mailSender;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final List<String> ALLOWED_ROLES = List.of("ADMIN", "USER", "TEACHER","STUDENT");
    private final EmailService emailService; // Injectez le nouveau service

    @PersistenceContext
    private EntityManager entityManager;

    @Value("${ebudget.frontend.reset-url}")
    private String frontendResetUrl;

    @Value("${ebudget.security.reset-token.expiration-hours}")
    private int tokenExpirationHours;

    public AuthService(AuthenticationManager authenticationManager,
                       UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       JwtUtils jwtUtils,
                       TokenBlacklistService tokenBlacklistService,
                       JavaMailSender mailSender,
                       PasswordResetTokenRepository passwordResetTokenRepository,
                       EmailService emailService) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
        this.tokenBlacklistService = tokenBlacklistService;
        this.mailSender = mailSender;
        this.passwordResetTokenRepository = passwordResetTokenRepository;
        this.emailService = emailService;
    }


    public JwtResponse authenticateUser(LoginRequest loginRequest, HttpServletResponse response) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        // Vérifier si l'utilisateur a un rôle autorisé
        boolean hasAllowedRole = userDetails.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("USER")
                        || auth.getAuthority().equals("ADMIN")
                        || auth.getAuthority().equals("TEACHER")
                        || auth.getAuthority().equals("STUDENT"));

        if (!hasAllowedRole) {
            throw new UnauthorizedRoleException("Vous n'êtes pas autorisé à vous connecter.");
        }

        List<String> roles = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();

        String jwt = jwtUtils.generateToken(userDetails);

        ResponseCookie cookie = ResponseCookie.from("jwt-token", jwt)
                .httpOnly(true)
                .secure(true) // à activer seulement en HTTPS
                .path("/")
                .maxAge(7200)
                .sameSite("Strict")
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        return new JwtResponse(jwt,roles);
    }



    public void logout(String token) {
        Date expirationDate = jwtUtils.extractExpiration(token);
        tokenBlacklistService.blacklistToken(token, expirationDate);
    }


    @Transactional // Important pour la gestion des tokens
    public void processForgotPassword(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé avec l'email: " + userEmail));

        // Supprimer les anciens tokens pour cet utilisateur (bonne pratique)
        passwordResetTokenRepository.deleteByUser(user);

        entityManager.flush();

        String token = UUID.randomUUID().toString();
        PasswordResetToken passwordResetToken = new PasswordResetToken(token, user, tokenExpirationHours);
        passwordResetTokenRepository.save(passwordResetToken);

        String resetUrl = frontendResetUrl + "?token=" + token;
        emailService.sendPasswordResetEmail(user.getEmail(), user.getLastName() + " "+ user.getFirstName(), resetUrl);
    }


    @Transactional
    public void resetPassword(String token, String newPassword) {
        PasswordResetToken resetToken = passwordResetTokenRepository.findByToken(token)
                .orElseThrow(() -> new InvalidTokenException("Token invalide ou non trouvé."));

        if (resetToken.isExpired()) {
            passwordResetTokenRepository.delete(resetToken); // Nettoyer le token expiré
            throw new TokenExpiredException("Le token de réinitialisation a expiré.");
        }

        User user = resetToken.getUser();
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        // Le token a été utilisé, on le supprime
        passwordResetTokenRepository.delete(resetToken);

        // Optionnel : Envoyer un email de confirmation de changement
        // emailService.sendPasswordChangeConfirmationEmail(user.getEmail(), user.getLastName() + " " +user.getFirstName());
    }

    @Transactional
    public void updatePassword(String userEmail, String currentPassword, String newPassword) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé: " + userEmail));

        // Vérifier si l'ancien mot de passe correspond
        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            throw new IllegalArgumentException("L'ancien mot de passe est incorrect.");
            // Ou une exception plus spécifique comme InvalidCredentialsException
        }

        // Mettre à jour avec le nouveau mot de passe hashé
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

    }

}
