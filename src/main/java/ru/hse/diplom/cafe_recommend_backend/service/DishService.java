package ru.hse.diplom.cafe_recommend_backend.service;

import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;
import org.springframework.stereotype.Service;
import ru.hse.diplom.cafe_recommend_backend.model.DishCategory;
import ru.hse.diplom.cafe_recommend_backend.model.Season;
import ru.hse.diplom.cafe_recommend_backend.model.dto.*;
import ru.hse.diplom.cafe_recommend_backend.model.entity.Dish;
import ru.hse.diplom.cafe_recommend_backend.model.entity.Ingredient;
import ru.hse.diplom.cafe_recommend_backend.repository.DishRepository;

import java.util.*;

import static ru.hse.diplom.cafe_recommend_backend.model.Constants.DISH_DOES_NOT_EXIST;
import static ru.hse.diplom.cafe_recommend_backend.service.Utils.getValueOfDefault;

@Service
@RequiredArgsConstructor
@Slf4j
public class DishService {
    private final DishRepository dishRepository;
    private final IngredientService ingredientService;
    private final OrderService orderService;

    public Integer getDishesCount() {
        return dishRepository.getDishesCount();
    }

    public DishDto get(UUID id) {
        return map(dishRepository.findById(id).orElseThrow());
    }

    public FullDishInfoDto getWithIngredients(UUID id) {
        return map(dishRepository.findById(id).orElseThrow(), IngredientService.map(ingredientService.getIngredientsByDishId(id).getIngredients()));
    }

    public List<FullDishInfoDto> getByIds(List<UUID> ids) {
        if (ids.isEmpty()) {
            return List.of();
        }
        return dishRepository.findByIds(ids).stream()
                .map(dish -> DishService.map(dish, List.of()))
                .toList();
    }

    public List<FullDishInfoDto> getPopular() {
        return dishRepository.findPopular()
                .stream()
                .sorted(Comparator.comparingLong(DishPopularityDto::getTotalOrders).reversed())
                .map(DishPopularityDto::getDish)
                .map(dish -> DishService.map(dish, List.of()))
                .toList();
    }

    public DishListDto getAll(Boolean withIngredients, Boolean onlyEnabled) {
        withIngredients = getValueOfDefault(withIngredients, false);
        onlyEnabled = getValueOfDefault(onlyEnabled, false);

        List<Dish> dishes;

        if (onlyEnabled) {
            dishes = dishRepository.findAllEnabled();
        } else {
            dishes = dishRepository.findAll();
        }

        if (withIngredients) {
            return DishListDto.of(
                    dishes.stream()
                            .map(dish -> FullDishInfoDto.builder()
                                    .id(dish.getId())
                                    .name(dish.getName())
                                    .description(dish.getDescription())
                                    .price(dish.getPrice())
                                    .enabled(dish.getEnabled())
                                    .category(dish.getCategory().getRusName())
                                    .season(dish.getSeason())
                                    .ingredients(ingredientService.getIngredientsByDishId(dish.getId()).getIngredients())
                                    .build()
                            )
                            .toList()
            );
        } else {
            return DishListDto.of(mapToFullDishInfoList(dishes));
        }
    }

    @Transactional
    public Map<UUID, Integer> getRatedDishes(UUID userId) {
        // Оценки: 0 - пользователь не пробовал блюдо, 1 - пробовал
        List<UUID> orderedDishes = orderService.getOrderedDishesId(userId);
        List<UUID> allDishes = dishRepository.findAllIds();
        Map<UUID, Integer> result = new HashMap<>();
        allDishes.forEach(dishId -> result.put(dishId, orderedDishes.contains(dishId) ? 1 : 0));
        return result;
    }

    @Transactional
    public RealVector getDishVector(UUID dishId) {
        List<UUID> ingredientIds = dishRepository
                .findIngredientsByDishId(dishId)
                .stream()
                .map(Ingredient::getId)
                .toList();
        return ingredientService.getRatedIngredientsVector(ingredientIds);
    }

    public RealVector getDefaultDishVector() {
        Integer dishesCount = getDishesCount();
        return new ArrayRealVector(dishesCount, 0);
    }

    @Transactional
    public DishDto add(NewDishDto dto) {
        Dish dish = Dish.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .price(dto.getPrice())
                .enabled(getValueOfDefault(dto.getEnabled(), true))
                .category(DishCategory.valueOfRusName(dto.getCategory()))
                .season(Season.valueOf(getValueOfDefault(dto.getSeason().toUpperCase(), Season.DEFAULT.name())))
                .build();
        Dish savedDish = dishRepository.save(dish);
        List<UUID> ingredientIds = dto.getIngredients().stream()
                .map(IngredientDto::getId)
                .toList();
        dishRepository.saveAllDishIngredientRef(savedDish.getId(), ingredientIds.toArray(new UUID[0]));
        return map(savedDish);
    }

    @Transactional
    public DishDto edit(DishDto newDish) {
        UUID id = newDish.getId();
        if (dishRepository.existsById(id)) {
            return map(dishRepository.save(map(newDish)));
        }
        throw new RuntimeException(String.format(DISH_DOES_NOT_EXIST, id));
    }

    @Transactional
    public void delete(UUID id) {
        if (!dishRepository.existsById(id)) {
            throw new RuntimeException(String.format(DISH_DOES_NOT_EXIST, id));
        }
        dishRepository.deleteById(id);
    }

    public static DishDto map(Dish dish) {
        return DishDto.builder()
                .id(dish.getId())
                .name(dish.getName())
                .description(dish.getDescription())
                .price(dish.getPrice())
                .enabled(dish.getEnabled())
                .category(dish.getCategory().getRusName())
                .season(dish.getSeason())
                .build();
    }

    public static Dish map(@NotNull DishDto dish) {
        return Dish.builder()
                .id(dish.getId())
                .name(dish.getName())
                .description(dish.getDescription())
                .price(dish.getPrice())
                .enabled(dish.isEnabled())
                .category(DishCategory.valueOfRusName(dish.getCategory()))
                .season(dish.getSeason())
                .build();
    }

    public static DishDto map(FullDishInfoDto dish) {
        return DishDto.builder()
                .id(dish.getId())
                .name(dish.getName())
                .description(dish.getDescription())
                .price(dish.getPrice())
                .enabled(dish.isEnabled())
                .category(dish.getCategory())
                .season(dish.getSeason())
                .build();
    }

    public static FullDishInfoDto map(@NotNull Dish dish, List<Ingredient> ingredients) {
        return FullDishInfoDto.builder()
                .id(dish.getId())
                .name(dish.getName())
                .description(dish.getDescription())
                .price(dish.getPrice())
                .enabled(dish.getEnabled())
                .category(dish.getCategory().getRusName())
                .season(dish.getSeason())
                .ingredients(ingredients == null || ingredients.isEmpty() ? List.of() : IngredientService.mapToDto(ingredients))
                .build();
    }

    public static List<DishDto> map(@NotNull List<Dish> dishes) {
        return dishes.stream()
                .map(DishService::map)
                .toList();
    }

    public static List<FullDishInfoDto> mapToFullDishInfoList(@NotNull List<Dish> dishes) {
        return dishes.stream()
                .map(dish -> DishService.map(dish, null))
                .toList();
    }

}
