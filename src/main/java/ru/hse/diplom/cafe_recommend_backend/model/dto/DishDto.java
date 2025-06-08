package ru.hse.diplom.cafe_recommend_backend.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Getter;
import ru.hse.diplom.cafe_recommend_backend.model.Season;

import java.util.UUID;

@Builder
@Getter
public class DishDto {
    @NotNull
    private UUID id;
    @NotBlank
    private String name;
    private String description;
    @Positive
    private Integer price;
    @NotNull
    private boolean enabled;
    @NotNull
    private String category;
    @NotNull
    private Season season;
}
