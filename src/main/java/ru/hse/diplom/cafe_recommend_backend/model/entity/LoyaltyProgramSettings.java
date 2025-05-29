package ru.hse.diplom.cafe_recommend_backend.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;

import java.time.OffsetDateTime;

@Builder
@Entity
@Getter
@Table(schema = "blues", name = "loyalty_program_settings")
// Настройки программы лояльности
public class LoyaltyProgramSettings {
    // Идентификатор (1 для единственной строки с настройками)
    @Id
    private int id = 1;

    // Базовый процент скидки для новых пользователей по программе лояльности
    @Column(name = "base_discount", nullable = false)
    private double baseDiscount;

    // Количество заказов, необходимое для повышения скидки
    @Column(name = "orders_threshold", nullable = false)
    private int ordersThreshold;

    // Процент повышения скидки, когда достигнуто количество заказов, необходимое для повышения
    @Column(name = "discount_increment", nullable = false)
    private double discountIncrement;

    // Максимально возможный процент скидки
    @Column(name = "max_discount", nullable = false)
    private double maxDiscount;

    // Дата и время последнего обновления настроек
    @Column(name = "updated_at")
    private OffsetDateTime updatedAt;

}
