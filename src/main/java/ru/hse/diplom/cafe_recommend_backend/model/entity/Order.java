package ru.hse.diplom.cafe_recommend_backend.model.entity;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Entity
@Table(schema = "blues", name = "orders")
// История заказов
public class Order {
    // Идентификатор заказа
    @Id
    @GeneratedValue
    private UUID id;

    // Идентификатор пользователя, сделавшего заказ
    @Column(name = "user_id", nullable = false)
    private UUID userId;

    // Время создания заказа
    @Column(name = "created_at")
    private OffsetDateTime createdAt;
}
