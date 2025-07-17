package com.elearningBackend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdatePasswordRequest {
    @NotBlank(message = "L'ancien mot de passe est requis")
    private String currentPassword;

    @NotBlank(message = "Le nouveau mot de passe ne peut pas être vide")
    @Size(min = 8, message = "Le mot de passe doit contenir au moins 8 caractères")
    // Ajoutez d'autres contraintes
    private String newPassword;

    @NotBlank(message = "La confirmation du nouveau mot de passe est requise")
    private String confirmPassword;
}
