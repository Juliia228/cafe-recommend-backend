package ru.hse.diplom.cafe_recommend_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.hse.diplom.cafe_recommend_backend.model.entity.OrderInfo;

import java.util.List;
import java.util.UUID;

public interface OrderInfoRepository extends JpaRepository<OrderInfo, UUID> {
    @Query(value = """
            SELECT oi.dishId 
            FROM OrderInfo oi 
            JOIN Order o 
            ON o.id = oi.orderId 
            WHERE o.userId = :userId
            """, nativeQuery = true)
    List<UUID> findOrderedDishIdByUserId(UUID userId);

}
