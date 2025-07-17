package com.elearningBackend.services;

import com.elearningBackend.models.BlacklistedToken;
import com.elearningBackend.repositories.BlacklistedTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class TokenBlacklistService {
    private final BlacklistedTokenRepository blacklistedTokenRepository;

    public boolean isTokenBlacklisted(String token) {
        return blacklistedTokenRepository.findByToken(token).isPresent();
    }

    @Transactional
    public void blacklistToken(String token, Date expirationDate) {
        if (!isTokenBlacklisted(token)) {
            BlacklistedToken blacklistedToken = new BlacklistedToken();
            blacklistedToken.setToken(token);
            blacklistedToken.setExpirationDate(expirationDate);
            blacklistedTokenRepository.save(blacklistedToken);
        }
    }

    @Scheduled(fixedRate = 3600000)
    @Transactional
    public void cleanExpiredTokens() {
        blacklistedTokenRepository.deleteAllExpiredSince(new Date());
    }
}