package ru.hse.diplom.cafe_recommend_backend.model.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;

import java.time.OffsetDateTime;
import java.util.UUID;

@Builder
@Data
@Entity
@Table(schema = "blues", name = "users")
public class User {
    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = "first_name", nullable = false, length = 20)
    private String firstName;

    @Column(name = "last_name", length = 20)
    private String lastName;

    @Column(nullable = false, unique = true, length = 20)
    private String phone;

    @Column(nullable = false)
    private String password;

    @Column(name = "key_word", nullable = false)
    private String keyWord;

    @Column(name = "loyalty_discount")
    private Integer loyaltyDiscount;

    @Column(name = "created_at")
    private OffsetDateTime createdAt;

//    private String[] roles;
}
