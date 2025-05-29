package ru.hse.diplom.cafe_recommend_backend.model.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import ru.hse.diplom.cafe_recommend_backend.model.DishCategory;
import ru.hse.diplom.cafe_recommend_backend.model.Season;

import java.util.UUID;

@Builder
@Entity
@Getter
@Table(schema = "blues", name = "dishes")
// Блюда кафе "Блюз"
public class Dish {
    // Идентификатор блюда
    @Id
    @GeneratedValue
    private UUID id;

    // Название блюда
    @Column(nullable = false)
    private String name;

    // Описание блюда
    private String description;

    // Цена блюда
    @Column(nullable = false)
    private Integer price;

    // Доступно ли блюдо для заказа
    @Column(nullable = false)
    private Boolean enabled;

    // Категория блюда
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private DishCategory category;

    // В какой сезон лучше рекомендовать блюдо. "DEFAULT" - блюдо подходит для любого сезона
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Season season;

}
