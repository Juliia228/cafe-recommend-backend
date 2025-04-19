package ru.hse.diplom.cafe_recommend_backend.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = "first_name", nullable = false, length = 20)
    private String firstName;

    @Column(name = "last_name", length = 20)
    private String lastName;

    @Column(name = "phone", nullable = false, unique = true, length = 20)

    private String phone;

    @Column(nullable = false)
    private String password;

    @Column(name = "loyalty_discount")
    private Integer loyaltyDiscount;

    @Column(name = "created_at")
    private OffsetDateTime createdAt;

//    private String[] roles;
}
