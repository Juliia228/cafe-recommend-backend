package ru.hse.diplom.cafe_recommend_backend.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.hse.diplom.cafe_recommend_backend.model.entity.Dish;

@AllArgsConstructor
@Getter
public class DishPopularityDto {
    private Dish dish;
    private Long totalOrders;
}
