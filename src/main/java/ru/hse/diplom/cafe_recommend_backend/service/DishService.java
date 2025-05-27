package ru.hse.diplom.cafe_recommend_backend.service;

import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.hse.diplom.cafe_recommend_backend.model.dto.DishDto;
import ru.hse.diplom.cafe_recommend_backend.model.dto.DishListDto;
import ru.hse.diplom.cafe_recommend_backend.model.dto.FullDishInfoDto;
import ru.hse.diplom.cafe_recommend_backend.model.entity.Dish;
import ru.hse.diplom.cafe_recommend_backend.model.entity.Ingredient;
import ru.hse.diplom.cafe_recommend_backend.repository.DishRepository;

import java.util.List;
import java.util.UUID;

import static ru.hse.diplom.cafe_recommend_backend.model.Constants.DISH_DOES_NOT_EXIST;

@Service
@RequiredArgsConstructor
public class DishService {
    private final DishRepository dishRepository;

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

    public DishListDto getAll(Boolean withIngredients) {
        if (withIngredients) {
//            List<> dishesWithIngredients = dishRepository.findAllDishesWithIngredients();
//            return DishListDto.of(map(dishesWithIngredients));
        }
        List<Dish> dishes = dishRepository.findAll();
        return DishListDto.of(mapToFullDishInfoList(dishes));
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
                .build();
    }

    public static Dish map(@NotNull DishDto dish) {
        return Dish.builder()
                .id(dish.getId())
                .name(dish.getName())
                .description(dish.getDescription())
                .price(dish.getPrice())
                .enabled(dish.isEnabled())
                .build();
    }

    public static FullDishInfoDto map(@NotNull Dish dish, List<Ingredient> ingredients) {
        return FullDishInfoDto.builder()
                .id(dish.getId())
                .name(dish.getName())
                .description(dish.getDescription())
                .price(dish.getPrice())
                .enabled(dish.isEnabled())
                .ingredients(ingredients.isEmpty() ? List.of() : IngredientService.map(ingredients))
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
