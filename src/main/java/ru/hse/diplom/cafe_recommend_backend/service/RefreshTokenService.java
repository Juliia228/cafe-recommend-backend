package ru.hse.diplom.cafe_recommend_backend.service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.hse.diplom.cafe_recommend_backend.model.entity.RefreshToken;
import ru.hse.diplom.cafe_recommend_backend.repository.RefreshTokenRepository;

import java.time.ZonedDateTime;
import java.util.UUID;

@AllArgsConstructor
@Service
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;

    @Transactional
    public RefreshToken createRefreshToken(UUID userId) {
        RefreshToken refreshToken = RefreshToken.builder()
                .token(UUID.randomUUID().toString())
                .expiration(ZonedDateTime.now().plusHours(12))
                .userId(userId)
                .build();
        return refreshTokenRepository.save(refreshToken);
    }

    public RefreshToken getByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    public boolean isTokenExpired(RefreshToken refreshToken) {
        if (refreshToken.getExpiration().isBefore(ZonedDateTime.now())) {
            refreshTokenRepository.delete(refreshToken);
            return true;
        }
        return false;
    }

}