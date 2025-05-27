package ru.hse.diplom.cafe_recommend_backend.model.dto;

import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor(staticName = "of")
public class IngredientListDto {
    private List<IngredientDto> ingredients;
}
