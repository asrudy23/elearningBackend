package com.elearningBackend.controllers;

import com.elearningBackend.dto.*;
import com.elearningBackend.exception.InvalidTokenException;
import com.elearningBackend.exception.TokenExpiredException;
import com.elearningBackend.security.UserDetailsImpl;
import com.elearningBackend.services.AuthService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> authenticateUser(@RequestBody LoginRequest loginRequest, HttpServletResponse response) {
        return ResponseEntity.ok(authService.authenticateUser(loginRequest, response));
    }

//    @PostMapping("/logout")
//    public ResponseEntity<Void> logout(@RequestHeader("Authorization") String authHeader) {
//        String token = authHeader.substring(7); // Supprime "Bearer "
//        authService.logout(token);
//        return ResponseEntity.ok().build();
//    }


    @PostMapping("/logout")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'TEACHER', 'STUDENT')")
    public ResponseEntity<Void> logout(@CookieValue("jwt-token") String token, HttpServletResponse response) {
        authService.logout(token);

        // Supprimer le cookie du client
        ResponseCookie cookie = ResponseCookie.from("jwt-token", "")
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(0)
                .sameSite("Strict")
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        return ResponseEntity.ok().build();
    }

    @PostMapping("/password/forgot")
    public ResponseEntity<?> forgotPassword(@Valid @RequestBody ForgotPasswordRequest forgotPasswordRequest) {
        try {
            authService.processForgotPassword(forgotPasswordRequest.getEmail());
            // Message générique pour des raisons de sécurité
            return ResponseEntity.ok("Si un compte correspondant à cet email existe, un lien de réinitialisation a été envoyé.");
        } catch (UsernameNotFoundException e) {
            // Ne pas révéler si l'email existe ou non au client, mais loguer peut être utile
            System.err.println("Forgot password attempt for non-existent email: " + forgotPasswordRequest.getEmail());
            return ResponseEntity.ok("Si un compte correspondant à cet email existe, un lien de réinitialisation a été envoyé.");
        } catch (Exception e) {
            System.err.println("Error processing forgot password request: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur lors du traitement de la demande.");
        }
    }


    @PostMapping("/password/reset")
    public ResponseEntity<?> resetPassword(@Valid @RequestBody ResetPasswordRequest resetPasswordRequest) {
        // Validation simple de la correspondance des mots de passe
        if (!resetPasswordRequest.getNewPassword().equals(resetPasswordRequest.getConfirmPassword())) {
            return ResponseEntity.badRequest().body("Les mots de passe ne correspondent pas.");
        }

        try {
            authService.resetPassword(resetPasswordRequest.getToken(), resetPasswordRequest.getNewPassword());
            return ResponseEntity.ok("Mot de passe réinitialisé avec succès.");
        } catch (InvalidTokenException | TokenExpiredException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            System.err.println("Error resetting password: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur lors de la réinitialisation du mot de passe.");
        }
    }

    @PostMapping("/password/update")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'TEACHER', 'STUDENT')") // Ajout de la restriction de rôle
    public ResponseEntity<?> updatePassword(
            @AuthenticationPrincipal UserDetailsImpl userDetails, // Récupère l'utilisateur connecté
            @Valid @RequestBody UpdatePasswordRequest updatePasswordRequest) {

        // Vérification que l'utilisateur est bien authentifié (normalement géré par Spring Security avant @PreAuthorize)
        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Utilisateur non authentifié.");
        }

        // Validation de la correspondance des nouveaux mots de passe
        if (!updatePasswordRequest.getNewPassword().equals(updatePasswordRequest.getConfirmPassword())) {
            return ResponseEntity.badRequest().body("Les nouveaux mots de passe ne correspondent pas.");
        }

        try {
            authService.updatePassword(
                    userDetails.getUsername(), // Utilise l'email de l'utilisateur authentifié
                    updatePasswordRequest.getCurrentPassword(),
                    updatePasswordRequest.getNewPassword()
            );
            return ResponseEntity.ok("Mot de passe mis à jour avec succès.");
        } catch (IllegalArgumentException e) { // Capturer l'erreur si l'ancien mot de passe est incorrect
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (UsernameNotFoundException e) { // Au cas où l'utilisateur serait supprimé entre-temps
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Utilisateur non trouvé.");
        }
        catch (Exception e) {
            System.err.println("Error updating password for user " + userDetails.getUsername() + ": " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur lors de la mise à jour du mot de passe.");
        }
    }

}
