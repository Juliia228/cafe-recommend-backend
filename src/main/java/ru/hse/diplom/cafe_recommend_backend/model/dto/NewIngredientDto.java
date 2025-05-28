package ru.hse.diplom.cafe_recommend_backend.model.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class NewIngredientDto {
    @NotBlank
    private String name;
}
