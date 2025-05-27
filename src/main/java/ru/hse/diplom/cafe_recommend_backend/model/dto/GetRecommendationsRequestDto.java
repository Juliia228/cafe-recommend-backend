package ru.hse.diplom.cafe_recommend_backend.model.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;

import java.util.UUID;

@Getter
public class GetRecommendationsRequestDto {
    @NotNull
    private UUID userId;

    @PositiveOrZero
    private int recommendationsCount;

}
