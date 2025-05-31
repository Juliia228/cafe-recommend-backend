package ru.hse.diplom.cafe_recommend_backend.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor(staticName = "of")
public class IngredientListDto {
    private List<IngredientDto> ingredients;
}
