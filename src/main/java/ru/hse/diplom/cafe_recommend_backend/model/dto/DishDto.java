package ru.hse.diplom.cafe_recommend_backend.model.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Builder
@Getter
public class DishDto {
    private UUID id;
    private String name;
    private String description;
    private int price;
    private boolean enabled;
    // TODO: add category
}
