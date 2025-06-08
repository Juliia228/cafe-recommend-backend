package ru.hse.diplom.cafe_recommend_backend.model.dto;

import lombok.Builder;

import java.time.ZonedDateTime;

@Builder
public record AuthResponseDto(
        UserWithRolesDto user,
        String accessToken,
        String refreshToken,
        ZonedDateTime refreshTokenExpiration
) {
}
