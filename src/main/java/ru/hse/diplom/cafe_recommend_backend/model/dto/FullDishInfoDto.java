package ru.hse.diplom.cafe_recommend_backend.model.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.UUID;

@Builder
@Getter
public class FullDishInfoDto {
    private UUID id;
    private String name;
    private String description;
    private int price;
    private boolean enabled;
    private List<IngredientDto> ingredients;
}
