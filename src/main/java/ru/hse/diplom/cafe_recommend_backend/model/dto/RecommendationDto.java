package ru.hse.diplom.cafe_recommend_backend.model.dto;

import lombok.Data;

@Data
public class RecommendationDto {
    private int item_id;
    private float score;
}
