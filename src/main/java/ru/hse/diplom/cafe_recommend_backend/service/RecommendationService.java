package ru.hse.diplom.cafe_recommend_backend.service;

import java.util.*;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.commons.math3.linear.RealVector;
import org.springframework.stereotype.Service;
import ru.hse.diplom.cafe_recommend_backend.model.Season;
import ru.hse.diplom.cafe_recommend_backend.model.dto.FullDishInfoDto;
import ru.hse.diplom.cafe_recommend_backend.model.dto.GetRecommendationsRequestDto;
import ru.hse.diplom.cafe_recommend_backend.model.dto.RecommendationsResponseDto;
import ru.hse.diplom.cafe_recommend_backend.model.entity.User;

import static ru.hse.diplom.cafe_recommend_backend.service.Utils.getCosineSimilarity;

@Service
@RequiredArgsConstructor
public class RecommendationService {
    private final UserService userService;
    private final DishService dishService;
    private final OrderService orderService;

    public RecommendationsResponseDto recommend(GetRecommendationsRequestDto request) {
        UUID userId = request.getUserId();
        RealVector userPreferences = userService.getUserPreferences(userId);

        List<UUID> contentBasedRecommendations = contentBasedRecommend(userPreferences, userId);
        List<UUID> collaborativeRecommendations = recommendCollaborative(userPreferences, userId);

        Set<UUID> result = new HashSet<>(contentBasedRecommendations);
        result.addAll(collaborativeRecommendations);

        List<UUID> recommendations = Objects.requireNonNull(addOtherFactors(result))
                .stream()
                .limit(request.getRecommendationsCount())
                .toList();

        List<FullDishInfoDto> recommendedDishes = dishService.getByIds(recommendations)
                .stream()
                .map(dish -> DishService.map(dish, List.of()))
                .toList();

        return RecommendationsResponseDto.of(recommendedDishes);
    }

    @Transactional
    public List<UUID> contentBasedRecommend(RealVector userPreferencesVector, UUID userId) {
        List<UUID> recommendations = new ArrayList<>();
        List<UUID> userOrderedDishes = orderService.getOrderedDishesId(userId);
        List<FullDishInfoDto> dishes = dishService.getAll(false).getDishes();

        dishes.forEach(dish -> {
            var dishId = dish.getId();
            if (!userOrderedDishes.contains(dishId)) {
                RealVector dishVector = dishService.getDishVector(dishId);
                if (userPreferencesVector.getDimension() != dishVector.getDimension()) {
                    throw new RuntimeException(String.format("Не удалось сравнить предпочтения пользователя при генерации рекомендаций. id пользователя = %s", userId));
                }

                double similarity = getCosineSimilarity(userPreferencesVector, dishVector);
                if (similarity > 0.5) {
                    recommendations.add(dishId);
                }
            }
        });
        return recommendations;
    }

    @Transactional
    public List<UUID> recommendCollaborative(RealVector userPreferences, UUID userId) {
        Map<UUID, Double> scores = new HashMap<>();
        var users = userService.getAll();

        for (User otherUser: users) {
            UUID otherUserId = otherUser.getId();
            if (!otherUserId.equals(userId)) {
                RealVector otherUserPreferences = userService.getUserPreferences(otherUserId);
                if (userPreferences.getDimension() != otherUserPreferences.getDimension()) {
                    throw new RuntimeException(String.format("Не удалось сравнить пользователей при генерации рекомендаций для пользователя с id = %s", userId));
                }

                double similarity = getCosineSimilarity(userPreferences, otherUserPreferences);

                Map<UUID, Integer> otherUserRatedDishes = dishService.getRatedDishes(userId);
                otherUserRatedDishes.forEach((dishId, rate) ->
                        scores.put(dishId, scores.getOrDefault(dishId, 0.0) + similarity));
            }
        }

        return scores.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .map(Map.Entry::getKey)
                .toList();
    }

    private List<UUID> addOtherFactors(Set<UUID> recommendations) {
        Season season = Season.getCurrentSeason();

        return null;
    }

}
