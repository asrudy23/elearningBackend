package com.elearningBackend.repositories;

import com.elearningBackend.models.BlacklistedToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Optional;

@Repository
public interface BlacklistedTokenRepository extends JpaRepository<BlacklistedToken, Long> {
    Optional<BlacklistedToken> findByToken(String token);

    @Transactional
    @Modifying
    @Query("DELETE FROM BlacklistedToken b WHERE b.expirationDate < ?1")
    void deleteAllExpiredSince(Date now);
}

