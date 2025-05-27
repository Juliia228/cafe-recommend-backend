package ru.hse.diplom.cafe_recommend_backend.service;

import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;
import org.springframework.stereotype.Service;
import ru.hse.diplom.cafe_recommend_backend.model.dto.IngredientDto;
import ru.hse.diplom.cafe_recommend_backend.model.dto.IngredientListDto;
import ru.hse.diplom.cafe_recommend_backend.model.entity.Ingredient;
import ru.hse.diplom.cafe_recommend_backend.repository.IngredientRepository;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static ru.hse.diplom.cafe_recommend_backend.model.Constants.INGREDIENT_DOES_NOT_EXIST;

@Service
@RequiredArgsConstructor
public class IngredientService {
    private final IngredientRepository ingredientRepository;
    private final DishService dishService;

    public IngredientDto get(UUID id) {
        return map(ingredientRepository.findById(id).orElseThrow());
    }

    public Map<UUID, Integer> getIngredientIndexes() {
//        return ingredientRepository.findArrayIndexes()
//                .stream()
//                .collect(Collectors.toMap(
//                        row -> (UUID) row[0],
//                        row -> (Integer) row[1]
//                ));
        return null;
    }

    public IngredientListDto getIngredientsByDishId(UUID dishId) {
        List<Ingredient> ingredients = ingredientRepository.findByDishId(dishId);
        return IngredientListDto.of(map(ingredients));
    }

    public IngredientListDto getAll() {
        List<Ingredient> ingredients = ingredientRepository.findAll();
        return IngredientListDto.of(map(ingredients));
    }

    @Transactional
    public IngredientDto edit(IngredientDto newIngredient) {
        UUID id = newIngredient.getId();
        if (ingredientRepository.existsById(id)) {
            return map(ingredientRepository.save(map(newIngredient)));
        }
        throw new RuntimeException(String.format(INGREDIENT_DOES_NOT_EXIST, id));
    }

    @Transactional
    public void delete(UUID id) {
        if (!ingredientRepository.existsById(id)) {
            throw new RuntimeException(String.format(INGREDIENT_DOES_NOT_EXIST, id));
        }
        ingredientRepository.deleteById(id);
    }

    public RealVector getEmptyIngredientVector() {
        int dishesCount = dishService.getDishesCount();
        return new ArrayRealVector(dishesCount, 0);
    }

    public static IngredientDto map(Ingredient ingredient) {
        return IngredientDto.builder()
                .id(ingredient.getId())
                .name(ingredient.getName())
                .build();
    }

    public static Ingredient map(IngredientDto ingredient) {
        return Ingredient.builder()
                .id(ingredient.getId())
                .name(ingredient.getName())
                .build();
    }

    public static List<IngredientDto> map(@NotNull List<Ingredient> ingredients) {
        return ingredients.stream()
                .map(IngredientService::map)
                .toList();
    }

}
