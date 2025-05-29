package ru.hse.diplom.cafe_recommend_backend.model;

public class Constants {
    // Patterns
    public static final String PHONE_REGEXP = "^(8|7|\\+7)((\\d{10})|((\\s)?((\\()?\\d{3}(\\))?)(\\s|-)?\\d{3}(\\s|-)?\\d{2}(\\s|-)?\\d{2}))$";
    public static final String PASSWORD_REGEXP = "^(?=.*[0-9])(?=.*[!@#$%^&*])(?=.*[a-z])(?=.*[A-Z])[0-9a-zA-Z!@#$%^&*]{8,}$";

    // Error messages
    public static final String USER_DOES_NOT_EXIST = "User with id = %s does not exist";
    public static final String DISH_DOES_NOT_EXIST = "Dish with id = %s does not exist";
    public static final String INGREDIENT_DOES_NOT_EXIST = "Ingredient with id = %s does not exist";

}
