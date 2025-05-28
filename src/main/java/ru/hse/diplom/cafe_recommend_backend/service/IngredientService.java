package ru.hse.diplom.cafe_recommend_backend.service;

import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.apache.commons.math3.linear.RealVector;
import org.springframework.stereotype.Service;
import ru.hse.diplom.cafe_recommend_backend.model.dto.IngredientDto;
import ru.hse.diplom.cafe_recommend_backend.model.dto.IngredientListDto;
import ru.hse.diplom.cafe_recommend_backend.model.dto.NewIngredientDto;
import ru.hse.diplom.cafe_recommend_backend.model.entity.Ingredient;
import ru.hse.diplom.cafe_recommend_backend.repository.IngredientRepository;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;

import static ru.hse.diplom.cafe_recommend_backend.model.Constants.INGREDIENT_DOES_NOT_EXIST;
import static ru.hse.diplom.cafe_recommend_backend.service.Utils.getVector;

@Service
@RequiredArgsConstructor
public class IngredientService {
    private final IngredientRepository ingredientRepository;

    public IngredientDto get(UUID id) {
        return map(ingredientRepository.findById(id).orElseThrow());
    }

    public IngredientListDto getIngredientsByDishId(UUID dishId) {
        List<Ingredient> ingredients = ingredientRepository.findByDishId(dishId);
        return IngredientListDto.of(map(ingredients));
    }

    public List<UUID> getDistinctOrderedIngredientIdsByUserId(UUID userId) {
        return ingredientRepository.findDistinctOrderedIngredientIds(userId);
    }

    public IngredientListDto getAll() {
        List<Ingredient> ingredients = ingredientRepository.findAll();
        return IngredientListDto.of(map(ingredients));
    }

    public IngredientDto add(NewIngredientDto dto) {
        Ingredient ingredient = Ingredient.builder()
                .name(dto.getName())
                .build();
        return map(ingredientRepository.save(ingredient));
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

    public RealVector getRatedIngredientsVector(List<UUID> ingredientIds) {
        List<UUID> allIngredients = ingredientRepository.findAllIds();
        Map<UUID, Integer> result = new TreeMap<>();
        allIngredients.forEach(ingredientId ->
                result.put(ingredientId, ingredientIds.contains(ingredientId) ? 1 : 0));
        return getVector(result);
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
