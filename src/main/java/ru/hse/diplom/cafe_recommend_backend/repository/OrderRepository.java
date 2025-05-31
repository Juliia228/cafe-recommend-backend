package ru.hse.diplom.cafe_recommend_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.hse.diplom.cafe_recommend_backend.model.dto.FullOrderInfoDto;
import ru.hse.diplom.cafe_recommend_backend.model.entity.Order;

import java.util.List;
import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<Order, UUID> {
    @Query("""
            SELECT new ru.hse.diplom.cafe_recommend_backend.model.dto.FullOrderInfoDto(o.id, o.userId, oi.dishId, oi.dishCount, o.createdAt) 
            FROM Order o 
            JOIN OrderInfo oi 
            ON o.id = oi.orderId
            """)
    List<FullOrderInfoDto> findAllWithOrderInfo();

    @Query("""
            SELECT COUNT(*) 
            FROM Order 
            WHERE userId = :userId
            """)
    int findOrdersCountByUserId(UUID userId);
}
