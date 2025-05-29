package ru.hse.diplom.cafe_recommend_backend.model.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;

import java.time.ZonedDateTime;
import java.util.UUID;

@Entity
@Table(schema = "blues", name = "tokens")
@Getter
@Builder
// Refresh токены
public class RefreshToken {
    // Идентификатор
    @Id
    @GeneratedValue
    private UUID id;

    // Refresh токен
    @Column(nullable = false)
    private String token;

    // Дата истечения срока токена
    @Column(nullable = false)
    private ZonedDateTime expiration;

    // Идентификатор пользователя
    @Column(name = "user_id", nullable = false)
    private UUID userId;
}
