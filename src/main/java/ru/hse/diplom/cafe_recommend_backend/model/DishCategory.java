package ru.hse.diplom.cafe_recommend_backend.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Getter
public enum DishCategory {
    SALADS_AND_COLD_APPETIZERS("салаты и холодные закуски"),
    SANDWICHES("бутерброды"),
    SOUPS("первые блюда"),
    FISH_DISHES("блюда из рыбы"),
    SHASHLIK("шашлык (весовой)"),
    MEAT_DISHES("мясные блюда"),
    SIDE_DISHES("гарнир"),
    SAUCES("соусы"),
    DESSERT("десерт"),
    CAKE("торт");

    private String rusName;

    public static List<String> getRusNames() {
        List<String> rusNames = new ArrayList<>();
        for (DishCategory category: values()) {
            rusNames.add(category.rusName);
        }
        return rusNames;
    }
}
