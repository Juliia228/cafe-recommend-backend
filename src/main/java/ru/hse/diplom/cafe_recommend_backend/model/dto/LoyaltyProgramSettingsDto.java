package ru.hse.diplom.cafe_recommend_backend.model.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Data;

import java.time.OffsetDateTime;

@Builder
@Data
public class LoyaltyProgramSettingsDto {
    @NotNull
    @Positive
    private double baseDiscount;
    @NotNull
    private int ordersThreshold;
    @NotNull
    @Positive
    private double discountIncrement;
    @NotNull
    @Positive
    private double maxDiscount;
    private OffsetDateTime updatedAt;
}
