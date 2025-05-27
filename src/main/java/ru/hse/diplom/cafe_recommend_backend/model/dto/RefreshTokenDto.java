//package ru.hse.diplom.cafe_recommend_backend.model.dto;
//
//import jakarta.persistence.*;
//import jakarta.validation.constraints.NotBlank;
//import jakarta.validation.constraints.NotNull;
//import lombok.Builder;
//import lombok.Getter;
//import ru.hse.diplom.cafe_recommend_backend.model.entity.User;
//
//import java.time.ZonedDateTime;
//import java.util.UUID;
//
//@Getter
//@Builder
//public class RefreshTokenDto {
//    @NotNull
//    private UUID id;
//
//    @NotBlank(message = "token is required")
//    private String token;
//
//    @NotNull(message = "expiration time is required")
//    private ZonedDateTime expiration;
//
//    @ManyToOne
//    @JoinColumn(name = "user_id", nullable = false)
//    private User user;
//}
