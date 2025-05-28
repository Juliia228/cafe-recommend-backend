package ru.hse.diplom.cafe_recommend_backend.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Getter;

@Getter
public class NewDishDto {
    @NotBlank
    private String name;
    private String description;
    @Positive
    private Integer price;
    private Boolean enabled;
    @NotBlank
    private String category;
    private String season;
}
