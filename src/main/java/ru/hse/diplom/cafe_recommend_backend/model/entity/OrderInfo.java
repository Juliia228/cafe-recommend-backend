package ru.hse.diplom.cafe_recommend_backend.model.entity;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(schema = "blues", name = "order_info")
public class OrderInfo {
    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = "order_id", nullable = false)
    private UUID orderId;

    @Column(name = "dish_id", nullable = false)
    private UUID dishId;

    @Column(name = "dish_count", nullable = false)
    private Integer dishCount = 1;

}
