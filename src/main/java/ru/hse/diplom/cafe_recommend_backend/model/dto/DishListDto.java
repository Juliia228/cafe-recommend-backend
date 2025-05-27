package ru.hse.diplom.cafe_recommend_backend.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor(staticName = "of")
@Getter
public class DishListDto {
    private List<FullDishInfoDto> dishes;
}
