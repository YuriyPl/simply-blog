package com.github.ypl.simplyblog.service;

import com.github.ypl.simplyblog.exception.TokenRefreshException;
import com.github.ypl.simplyblog.model.RefreshToken;
import com.github.ypl.simplyblog.repository.RefreshTokenRepository;
import com.github.ypl.simplyblog.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenService {

    @Value("${ypl.app.jwtRefreshExpirationMs}")
    private Long refreshTokenDurationMs;

    @Value("${ypl.app.limitOfTokensPerUser}")
    private Integer limitOfTokensPerUser;

    private final RefreshTokenRepository refreshTokenRepository;

    private final UserRepository userRepository;

    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository, UserRepository userRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.userRepository = userRepository;
    }

    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    @Transactional
    public RefreshToken createRefreshToken(int userId) {
        checkTokenAmount(userId);

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUser(userRepository.getReferenceById(userId));
        refreshToken.setExpiryDate(Instant.now().plusMillis(refreshTokenDurationMs));
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken = refreshTokenRepository.save(refreshToken);
        return refreshToken;
    }

    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(token);
            throw new TokenRefreshException(token.getToken(), "refresh token was expired. please make a new sign in request.");
        }

        return token;
    }

    @Transactional
    public void deleteAllByUserId(int userId) {
        refreshTokenRepository.deleteAllByUserId(userId);
    }

    private void checkTokenAmount(int userId) {
        if (refreshTokenRepository.countByUserId(userId) > limitOfTokensPerUser) {
            deleteAllByUserId(userId);
        }
    }
}
