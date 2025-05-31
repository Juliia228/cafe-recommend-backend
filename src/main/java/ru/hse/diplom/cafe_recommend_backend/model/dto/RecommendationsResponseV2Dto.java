package ru.hse.diplom.cafe_recommend_backend.model.dto;

import lombok.Data;

import java.util.List;

@Data
public class RecommendationsResponseV2Dto {
    List<RecommendationDto> recommendations;
}
