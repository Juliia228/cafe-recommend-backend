package ru.hse.diplom.cafe_recommend_backend.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@AllArgsConstructor(staticName = "of")
@Data
public class RecommendationsResponseDto {
    private List<DishDto> recommendations;

}
