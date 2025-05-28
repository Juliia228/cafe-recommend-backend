package ru.hse.diplom.cafe_recommend_backend.model.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.util.UUID;

@Getter
public class GetRecommendationsRequestDto {
    @NotNull
    private UUID userId;

    private Integer recommendationsCount;

}
