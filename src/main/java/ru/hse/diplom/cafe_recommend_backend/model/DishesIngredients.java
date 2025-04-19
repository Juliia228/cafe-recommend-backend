package ru.hse.diplom.cafe_recommend_backend.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Data
@Entity
@Table(name = "ref_dishes_ingredients")
public class DishesIngredients {
    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = "dish_id", nullable = false)
    private UUID dishId;

    @Column(name = "ingredient_id", nullable = false)
    private UUID ingredientId;
}
