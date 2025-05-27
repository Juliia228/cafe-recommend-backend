package ru.hse.diplom.cafe_recommend_backend.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

import java.time.ZonedDateTime;
import java.util.UUID;

@Getter
@Builder
public class RefreshTokenRequestDto {
    @NotBlank(message = "token is required")
    private String token;

    @NotNull(message = "expiration time is required")
    private ZonedDateTime expiration;

    @NotNull(message = "user_id is required")
    private UUID userId;
}
