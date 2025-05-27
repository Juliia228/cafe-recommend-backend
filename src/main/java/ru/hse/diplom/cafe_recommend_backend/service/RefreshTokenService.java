package ru.hse.diplom.cafe_recommend_backend.service;

import io.jsonwebtoken.JwtException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import ru.hse.diplom.cafe_recommend_backend.model.entity.RefreshToken;
import ru.hse.diplom.cafe_recommend_backend.model.entity.User;
import ru.hse.diplom.cafe_recommend_backend.model.UserDetailsImpl;
import ru.hse.diplom.cafe_recommend_backend.model.dto.TokenResponseDto;
import ru.hse.diplom.cafe_recommend_backend.repository.RefreshTokenRepository;
import ru.hse.diplom.cafe_recommend_backend.repository.UserRepository;

import java.time.ZonedDateTime;
import java.util.UUID;

@Service
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;
    private final JwtService jwtService;

    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository, UserRepository userRepository, JwtService jwtService) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.userRepository = userRepository;
        this.jwtService = jwtService;
    }

    @Transactional
    public TokenResponseDto generateAccessToken(String requestRefreshToken) {
        RefreshToken refreshToken = getByToken(requestRefreshToken);
        if (isTokenExpired(refreshToken)) {
            throw new JwtException("Refresh token is expired. Please log in");
        }

        User user = userRepository.findById(refreshToken.getUserId()).orElseThrow();
        String new_token = jwtService.generateToken(new UserDetailsImpl(user));
        return TokenResponseDto.builder()
                .access_token(new_token)
                .refresh_token(requestRefreshToken)
                .build();
    }

    @Transactional
    public RefreshToken createRefreshToken(String phone) {
        User user = userRepository.findByPhone(phone).orElseThrow();
        RefreshToken refreshToken = RefreshToken.builder()
                .token(UUID.randomUUID().toString())
                .expiration(ZonedDateTime.now().plusHours(12))
                .userId(user.getId())
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