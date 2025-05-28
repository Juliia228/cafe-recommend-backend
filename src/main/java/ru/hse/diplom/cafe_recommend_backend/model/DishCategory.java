package ru.hse.diplom.cafe_recommend_backend.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum DishCategory {
    SALADS_AND_COLD_APPETIZERS("Салаты и Холодные закуски"),
    SANDWICHES("Бутерброды"),
    SOUPS("Первые блюда"),
    FISH_DISHES("Блюда из рыбы"),
    SHASHLIK("Шашлык (весовой)"),
    MEAT_DISHES("Meat dishes"),
    SIDE_DISHES("Side dishes"),
    SAUCES("Соусы"),
    DESSERT("Десерт"),
    CAKE("Торт");

    private String rusName;
}
