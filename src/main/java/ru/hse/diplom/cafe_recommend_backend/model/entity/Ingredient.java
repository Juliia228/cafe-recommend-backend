package ru.hse.diplom.cafe_recommend_backend.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Getter
@Table(schema = "blues", name = "ingredients")
// Ингредиенты
public class Ingredient {
    // Идентификатор ингредиента
    @Id
    @GeneratedValue
    private UUID id;

    // Название ингредиента на русском языке
    @Column(nullable = false)
    private String name;
}
