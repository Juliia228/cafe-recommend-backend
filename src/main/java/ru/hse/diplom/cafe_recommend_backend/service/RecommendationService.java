package ru.hse.diplom.cafe_recommend_backend.service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.math3.linear.RealVector;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import ru.hse.diplom.cafe_recommend_backend.model.Season;
import ru.hse.diplom.cafe_recommend_backend.model.dto.*;
import ru.hse.diplom.cafe_recommend_backend.model.entity.User;

import static ru.hse.diplom.cafe_recommend_backend.service.Utils.createIndexMap;
import static ru.hse.diplom.cafe_recommend_backend.service.Utils.getCosineSimilarity;

@Service
@Slf4j
@RequiredArgsConstructor
public class RecommendationService {
    private final UserService userService;
    private final DishService dishService;
    private final OrderService orderService;
    private final WebClient webClient;

    @EventListener(ContextRefreshedEvent.class)
    public void onApplicationEvent() {
        log.info("Sending request /fit-collaborative-filtering-model to fit ALS model");
        fitCollaborativeFilteringModel();

        log.info("Sending request /fit-content-based-model to fit Word2Vec model");
        fitContentBasedModel();
    }

    @Transactional
    public RecommendationsResponseDto recommendV1(GetRecommendationsRequestDto request) {
        UUID userId = request.getUserId();
        RealVector userPreferences = userService.getUserPreferences(userId);

        List<UUID> contentBasedRecommendations = contentBasedRecommendV1(userPreferences, userId);
        List<UUID> collaborativeRecommendations = recommendCollaborativeV1(userPreferences, userId);

        Set<UUID> result = new HashSet<>(contentBasedRecommendations);
        result.addAll(collaborativeRecommendations);

        if (result.isEmpty()) {
            return RecommendationsResponseDto.of(List.of());
        }

        List<FullDishInfoDto> recommendedDishes = dishService.getByIds(result.stream().toList());
        recommendedDishes = addOtherFactors(recommendedDishes);

        Integer count = request.getRecommendationsCount();
        if (count != null) {
            if (recommendedDishes.size() < count) {
                recommendedDishes = Stream.concat(
                                recommendedDishes.stream(),
                                dishService.getPopular().stream())
                        .distinct()
                        .toList();
            }
            recommendedDishes = recommendedDishes.stream().limit(count).toList();
        }
        return RecommendationsResponseDto.of(recommendedDishes);
    }

    @Transactional
    public List<UUID> contentBasedRecommendV1(RealVector userPreferencesVector, UUID userId) {
        List<UUID> recommendations = new ArrayList<>();
        List<UUID> userOrderedDishes = orderService.getOrderedDishesId(userId);
        List<FullDishInfoDto> dishes = dishService.getAll(false, false).getDishes();

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
    public List<UUID> recommendCollaborativeV1(RealVector userPreferences, UUID userId) {
        Map<UUID, Double> scores = new HashMap<>();
        var users = userService.getAll();

        for (User otherUser : users) {
            UUID otherUserId = otherUser.getId();
            if (!otherUserId.equals(userId)) {
                RealVector otherUserPreferences = userService.getUserPreferences(otherUserId);
                if (userPreferences.getDimension() != otherUserPreferences.getDimension()) {
                    throw new RuntimeException(String.format("Не удалось сравнить пользователей при генерации рекомендаций для пользователя с id = %s", userId));
                }

                double similarity = getCosineSimilarity(userPreferences, otherUserPreferences);

                Map<UUID, Integer> otherUserRatedDishes = dishService.getRatedDishes(userId);
                otherUserRatedDishes.forEach((dishId, rate) ->
                        scores.put(dishId, scores.getOrDefault(dishId, 0.0) + rate + similarity));
            }
        }

        return scores.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .map(Map.Entry::getKey)
                .toList();
    }

    @Transactional
    public RecommendationsResponseDto recommendV2(GetRecommendationsRequestDto request) {
        // присваиваем числовые индексы идентификаторам пользователей и блюд
        Map<UUID, Integer> userIndexMap = userService.createUserIndexMap();

        List<FullDishInfoDto> allDishes = dishService.getAll(false, false).getDishes();
        List<UUID> allDishesIds = allDishes
                .stream()
                .map(FullDishInfoDto::getId)
                .toList();

        int userId = userIndexMap.get(request.getUserId());
        return RecommendationsResponseDto.of(sendRequestToGetRecommendations(new GetRecommendationsRequestV2Dto(userId))
                .getRecommendations()
                .stream()
                .sorted(Comparator.comparingDouble(RecommendationDto::getScore).reversed())
                .map(recommendation -> {
                    UUID dishId = allDishesIds.get(recommendation.getItem_id());
                    FullDishInfoDto dish = allDishes.stream()
                            .filter(dishInfo -> dishInfo.getId().equals(dishId))
                            .findFirst()
                            .orElseThrow();
                    return dish;
                })
                .toList());
    }

    @Transactional
    public void fitCollaborativeFilteringModel() {
        // присваиваем числовые индексы идентификаторам пользователей и блюд
        Map<UUID, Integer> userIndexMap = userService.createUserIndexMap();

        List<FullDishInfoDto> allDishes = dishService.getAll(false, false).getDishes();
        List<UUID> allDishesIds = allDishes
                .stream()
                .map(FullDishInfoDto::getId)
                .toList();
        Map<UUID, Integer> dishIndexMap = createIndexMap(allDishesIds);

        // создаем матрицу взаимодействий
        UsersDishesMatrix matrix = getUsersDishesMatrix(userIndexMap, dishIndexMap);
        sendRequestToFitCollaborativeFilteringModel(matrix);
    }

    @Transactional
    public UsersDishesMatrix getUsersDishesMatrix(Map<UUID, Integer> userIndexMap, Map<UUID, Integer> dishIndexMap) {
        List<List<Integer>> matrix = new ArrayList<>();

        // заполняем матрицу нулями
        for (int i = 0; i < userIndexMap.size(); i++) {
            matrix.add(new ArrayList<>(Collections.nCopies(dishIndexMap.size(), 0)));
        }

        List<FullOrderInfoDto> allOrdersFullInfo = orderService.getAllFullOrders();
        allOrdersFullInfo.forEach(orderInfo ->
                {
                    Integer userIdx = userIndexMap.get(orderInfo.getUserId());
                    Integer dishIdx = dishIndexMap.get(orderInfo.getDishId());
                    if (userIdx != null && dishIdx != null) {
                        matrix.get(userIdx).set(dishIdx, 1);  // 1 - пользователь заказал блюдо
                    }
                }
        );

        return UsersDishesMatrix.of(matrix);
    }

    public void fitContentBasedModel() {
        List<FullDishInfoDto> allDishes = dishService.getAll(true, false).getDishes();
        List<List<String>> dishesIngredients = allDishes.stream()
                .map(dish -> dish.getIngredients().stream()
                        .map(IngredientDto::getName)
                        .collect(Collectors.toList()))
                .toList();

        sendRequestToFitContentBasedModel(DishIngredientsListDto.of(dishesIngredients));
    }

    public RecommendationsResponseV2Dto sendRequestToGetRecommendations(GetRecommendationsRequestV2Dto request) {
        return webClient.post()
                .uri("/recommend")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(RecommendationsResponseV2Dto.class)
                .block();
    }

    public void sendRequestToFitCollaborativeFilteringModel(UsersDishesMatrix userItemMatrix) {
        MessageDto response = webClient.post()
                .uri("/fit-collaborative-filtering-model")
                .bodyValue(userItemMatrix)
                .retrieve()
                .bodyToMono(MessageDto.class)
                .block();
        if (response == null) {
            log.warn("Response from /fit-collaborative-filtering-model is null");
        } else {
            log.info("Response from /fit-collaborative-filtering-model: {}", response.getMessage());
        }
    }

    public void sendRequestToFitContentBasedModel(DishIngredientsListDto dto) {
        MessageDto response = webClient.post()
                .uri("/fit-content-based-model")
                .bodyValue(dto)
                .retrieve()
                .bodyToMono(MessageDto.class)
                .block();
        if (response == null) {
            log.warn("Response from /fit-content-based-model is null");
        } else {
            log.info("Response from /fit-content-based-model: {}", response.getMessage());
        }
    }

    private List<FullDishInfoDto> addOtherFactors(List<FullDishInfoDto> recommendations) {
        if (recommendations == null || recommendations.isEmpty()) {
            return List.of();
        }

        Season currentSeason = Season.getCurrentSeason();
        return recommendations.stream()
                .filter(dishInfo ->
                        dishInfo.getSeason().equals(currentSeason) || dishInfo.getSeason().equals(Season.DEFAULT))
                .toList();
    }

}
