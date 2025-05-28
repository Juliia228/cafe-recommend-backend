package ru.hse.diplom.cafe_recommend_backend.model.dto;

import lombok.Builder;
import lombok.Getter;
import ru.hse.diplom.cafe_recommend_backend.model.DishCategory;
import ru.hse.diplom.cafe_recommend_backend.model.Season;

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
    private DishCategory category;
    private Season season;
    private List<IngredientDto> ingredients;
}
