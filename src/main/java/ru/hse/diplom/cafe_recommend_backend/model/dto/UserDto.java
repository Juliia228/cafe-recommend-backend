package ru.hse.diplom.cafe_recommend_backend.model.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.OffsetDateTime;
import java.util.UUID;

@Builder
@Getter
public class UserDto {
    private UUID id;
    private String firstName;
    private String lastName;
    private String phone;
    private Double loyaltyDiscount;
    private OffsetDateTime createdAt;
}
