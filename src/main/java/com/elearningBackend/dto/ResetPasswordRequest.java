package com.elearningBackend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ResetPasswordRequest {
    @NotBlank(message = "Le token est requis")
    private String token;

    @NotBlank(message = "Le nouveau mot de passe ne peut pas être vide")
    @Size(min = 8, message = "Le mot de passe doit contenir au moins 8 caractères")
    // Ajoutez d'autres contraintes de validation si nécessaire (regex pour complexité)
    private String newPassword;

    @NotBlank(message = "La confirmation du mot de passe ne peut pas être vide")
    private String confirmPassword;
}
