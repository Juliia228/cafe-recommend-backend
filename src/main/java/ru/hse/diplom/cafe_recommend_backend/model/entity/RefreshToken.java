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
public class RefreshToken {
    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private String token;

    @Column(nullable = false)
    private ZonedDateTime expiration;

    @Column(name = "user_id", nullable = false)
    private UUID userId;
}
