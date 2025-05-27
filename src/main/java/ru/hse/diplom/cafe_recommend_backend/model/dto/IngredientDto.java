package ru.hse.diplom.cafe_recommend_backend.model.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Builder
@Getter
public class IngredientDto {
    private UUID id;
    private String name;
}
