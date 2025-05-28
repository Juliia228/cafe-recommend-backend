package ru.hse.diplom.cafe_recommend_backend.service;

import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;
import org.springframework.stereotype.Service;
import ru.hse.diplom.cafe_recommend_backend.model.DishCategory;
import ru.hse.diplom.cafe_recommend_backend.model.Season;
import ru.hse.diplom.cafe_recommend_backend.model.dto.DishDto;
import ru.hse.diplom.cafe_recommend_backend.model.dto.DishListDto;
import ru.hse.diplom.cafe_recommend_backend.model.dto.FullDishInfoDto;
import ru.hse.diplom.cafe_recommend_backend.model.dto.NewDishDto;
import ru.hse.diplom.cafe_recommend_backend.model.entity.Dish;
import ru.hse.diplom.cafe_recommend_backend.model.entity.Ingredient;
import ru.hse.diplom.cafe_recommend_backend.repository.DishRepository;

import java.util.*;

import static ru.hse.diplom.cafe_recommend_backend.model.Constants.DISH_DOES_NOT_EXIST;
import static ru.hse.diplom.cafe_recommend_backend.service.Utils.getValueOfDefault;

@Service
@RequiredArgsConstructor
public class DishService {
    private final DishRepository dishRepository;
    private final IngredientService ingredientService;
    private final OrderService orderService;

    public int getDishesCount() {
        return dishRepository.getDishesCount();
    }

    public DishDto get(UUID id) {
        return mapToDto(dishRepository.findById(id).orElseThrow());
    }

    public FullDishInfoDto getWithIngredients(UUID id) {
        // TODO
        return null;
    }

    public List<FullDishInfoDto> getByIds(List<UUID> ids) {
        if (ids.isEmpty()) {
            return List.of();
        }
        return dishRepository.findByIds(ids).stream()
                .map(dish -> map(dish, null))
                .toList();
    }

    public DishListDto getAll(Boolean withIngredients) {
        if (withIngredients) {
//            List<> dishesWithIngredients = dishRepository.findAllDishesWithIngredients();
//            return DishListDto.of(map(dishesWithIngredients));
        }
        List<Dish> dishes = dishRepository.findAll();
        return DishListDto.of(mapToFullDishInfoList(dishes));
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
        int dishesCount = getDishesCount();
        return new ArrayRealVector(dishesCount, 0);
    }

    public DishDto add(NewDishDto dto) {
        Dish dish = Dish.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .price(dto.getPrice())
                .enabled(getValueOfDefault(dto.getEnabled(), false))
                .category(DishCategory.valueOf(dto.getCategory()))
                .season(Season.valueOf(getValueOfDefault(dto.getSeason(), Season.DEFAULT.name())))
                .build();
        return mapToDto(dishRepository.save(dish));
    }

    @Transactional
    public DishDto edit(DishDto newDish) {
        UUID id = newDish.getId();
        if (dishRepository.existsById(id)) {
            return mapToDto(dishRepository.save(map(newDish)));
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

    public static DishDto mapToDto(Dish dish) {
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

    public static Dish map(@NotNull DishDto dish) {
        return Dish.builder()
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
                .enabled(dish.isEnabled())
                .category(dish.getCategory())
                .season(dish.getSeason())
                .ingredients(ingredients == null || ingredients.isEmpty() ? List.of() : IngredientService.map(ingredients))
                .build();
    }

    public static List<DishDto> map(@NotNull List<Dish> dishes) {
        return dishes.stream()
                .map(DishService::mapToDto)
                .toList();
    }

    public static List<FullDishInfoDto> mapToFullDishInfoList(@NotNull List<Dish> dishes) {
        return dishes.stream()
                .map(dish -> DishService.map(dish, null))
                .toList();
    }

}
