package ru.hse.diplom.cafe_recommend_backend.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.OffsetDateTime;
import java.util.UUID;

@AllArgsConstructor
@Getter
public class FullOrderInfoDto {
    private UUID orderId;
    private UUID userId;
    private UUID dishId;
    private Integer dishCount;
    private OffsetDateTime createdAt;
}
