package com.elearningBackend.repositories;

import com.elearningBackend.models.PasswordResetToken;
import com.elearningBackend.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {
    Optional<PasswordResetToken> findByToken(String token);
    void deleteByUser(User user); // Pour supprimer les anciens tokens d'un utilisateur
}
