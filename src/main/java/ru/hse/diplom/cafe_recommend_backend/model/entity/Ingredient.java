package ru.hse.diplom.cafe_recommend_backend.model.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Builder
@Entity
@Getter
@Table(schema = "blues", name = "ingredients")
public class Ingredient {
    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private String name;
}
