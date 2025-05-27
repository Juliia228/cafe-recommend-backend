package ru.hse.diplom.cafe_recommend_backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.hse.diplom.cafe_recommend_backend.repository.OrderInfoRepository;
import ru.hse.diplom.cafe_recommend_backend.repository.OrderRepository;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderInfoRepository orderInfoRepository;
    private final OrderRepository orderRepository;

    public List<UUID> getOrderedDishesId(UUID userId) {
        return orderInfoRepository.findOrderedDishIdByUserId(userId);
    }

}
