package ru.hse.diplom.cafe_recommend_backend.model.dto;

import lombok.Builder;
import ru.hse.diplom.cafe_recommend_backend.model.entity.User;

import java.time.ZonedDateTime;

@Builder
public record AuthResponseDto(
        User user,
        String accessToken,
        String refreshToken,
        ZonedDateTime refreshTokenExpiration
) {
}
