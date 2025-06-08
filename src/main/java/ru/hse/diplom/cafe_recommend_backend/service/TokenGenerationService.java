package ru.hse.diplom.cafe_recommend_backend.service;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import ru.hse.diplom.cafe_recommend_backend.model.dto.AuthResponseDto;
import ru.hse.diplom.cafe_recommend_backend.model.entity.RefreshToken;
import ru.hse.diplom.cafe_recommend_backend.model.entity.User;

import java.security.Key;
import java.util.*;

@Component
@RequiredArgsConstructor
public class TokenGenerationService {
    @Value("${jwt.secret}")
    private String jwtSecret;
    private final RefreshTokenService refreshTokenService;

    public AuthResponseDto createTokens(User user, UserDetails userDetails) {
        String jwtToken = generateToken(userDetails, user.getId());
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user.getId());
        return AuthResponseDto.builder()
                .user(UserService.mapUserWithRolesDto(user))
                .accessToken(jwtToken)
                .refreshToken(refreshToken.getToken())
                .refreshTokenExpiration(refreshToken.getExpiration())
                .build();
    }

    public String generateToken(UserDetails userDetails, UUID userId) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("user_id", userId);
        claims.put("roles", userDetails.getAuthorities());
        return createToken(claims, userDetails);
    }

    private String createToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        return Jwts.builder()
                .claims(extraClaims)
                .subject(userDetails.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 15)) // 15 минут
                .signWith(getSigningKey(), SignatureAlgorithm.HS256).compact();
    }

    public Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public RefreshToken getRefreshTokenEntity(String requestRefreshToken) {
        RefreshToken refreshToken = refreshTokenService.getByToken(requestRefreshToken);
        if (refreshTokenService.isTokenExpired(refreshToken)) {
            throw new JwtException("Refresh token is expired. Please log in");
        }
        return refreshToken;
    }

}

