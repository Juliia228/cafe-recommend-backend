package ru.hse.diplom.cafe_recommend_backend.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Getter
public enum DishCategory {
    SALADS_AND_COLD_APPETIZERS("Салаты и холодные закуски"),
    SANDWICHES("Бутерброды"),
    SOUPS("Первые блюда"),
    FISH_DISHES("Блюда из рыбы"),
    SHASHLIK("Шашлык (весовой)"),
    MEAT_DISHES("Мясные блюда"),
    SIDE_DISHES("Гарнир"),
    SAUCES("Соусы"),
    DESSERT("Десерт"),
    CAKE("Торт");

    private String rusName;

    public static List<String> getRusNames() {
        List<String> rusNames = new ArrayList<>();
        for (DishCategory category: values()) {
            rusNames.add(category.rusName);
        }
        return rusNames;
    }

    public static DishCategory valueOfRusName(String rusName) {
        for (DishCategory category: values()) {
            if (category.rusName.equals(getCorrectRusName(rusName))) {
                return category;
            }
        }
        throw new RuntimeException(String.format("Category with name %s don't exist", rusName));
    }

    public static String getCorrectRusName(String rusName) {
        return rusName.substring(0, 1).toUpperCase() + rusName.substring(1).toLowerCase();
    }
}
