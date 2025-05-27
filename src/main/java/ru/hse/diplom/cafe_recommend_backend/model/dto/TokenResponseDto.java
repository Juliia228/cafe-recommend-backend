package ru.hse.diplom.cafe_recommend_backend.model.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TokenResponseDto {
    private String access_token;
    private String refresh_token;
}