package ru.hse.diplom.cafe_recommend_backend.model.entity;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(schema = "blues", name = "order_info")
// Детали заказов
public class OrderInfo {
    // Идентификатор
    @Id
    @GeneratedValue
    private UUID id;

    // Идентификатор заказа
    @Column(name = "order_id", nullable = false)
    private UUID orderId;

    // Идентификатор блюда
    @Column(name = "dish_id", nullable = false)
    private UUID dishId;

    // Количество блюд (по dish_id)
    @Column(name = "dish_count", nullable = false)
    private Integer dishCount = 1;

}
