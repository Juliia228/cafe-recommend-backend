package ru.hse.diplom.cafe_recommend_backend.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.UUID;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(schema = "blues", name = "users")
// Пользователи
public class User {
    // Идентификатор пользователя
    @Id
    @GeneratedValue
    private UUID id;

    // Имя
    @Column(name = "first_name", nullable = false, length = 20)
    private String firstName;

    // Фамилия
    @Column(name = "last_name", length = 20)
    private String lastName;

    // Номер телефона
    @Column(nullable = false, unique = true, length = 20)
    private String phone;

    // Зашифрованный пароль
    @Column(nullable = false)
    private String password;

    // Зашифрованное кодовое слово для восстановления пароля
    @Column(name = "key_word", nullable = false)
    private String keyWord;

    // Размер персональной скидки
    @Column(name = "loyalty_discount")
    private Double loyaltyDiscount;

    // Количество повышений персональной скидки пользователя
    @Column(name = "loyalty_discount_increment_count")
    private Integer loyaltyDiscountIncrementCount;

    // Дата, время регистрации
    @Column(name = "created_at")
    private OffsetDateTime createdAt;

    // Роли пользователя
    private String[] roles;

}
