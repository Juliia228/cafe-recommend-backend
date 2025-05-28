package ru.hse.diplom.cafe_recommend_backend.model.dto;

import lombok.Builder;
import lombok.Getter;
import ru.hse.diplom.cafe_recommend_backend.model.DishCategory;
import ru.hse.diplom.cafe_recommend_backend.model.Season;

import java.util.UUID;

@Builder
@Getter
public class DishDto {
    private UUID id;
    private String name;
    private String description;
    private int price;
    private boolean enabled;
    private DishCategory category;
    private Season season;
}
