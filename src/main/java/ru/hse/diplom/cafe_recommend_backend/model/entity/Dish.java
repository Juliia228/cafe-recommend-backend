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
public class Dish {
    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private String name;

    private String description;

    @Column(nullable = false)
    private Integer price;

    @Column(nullable = false)
    private Boolean enabled;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private DishCategory category;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Season season;

}
