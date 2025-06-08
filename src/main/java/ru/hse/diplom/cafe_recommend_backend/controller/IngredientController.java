package ru.hse.diplom.cafe_recommend_backend.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.hse.diplom.cafe_recommend_backend.model.dto.IngredientDto;
import ru.hse.diplom.cafe_recommend_backend.model.dto.IngredientListDto;
import ru.hse.diplom.cafe_recommend_backend.model.dto.NewIngredientDto;
import ru.hse.diplom.cafe_recommend_backend.service.IngredientService;

import java.util.UUID;

import static ru.hse.diplom.cafe_recommend_backend.controller.IngredientController.INGREDIENT_REST_POINT;

@CrossOrigin
@RequestMapping(INGREDIENT_REST_POINT)
@RequiredArgsConstructor
@RestController
@Slf4j
@Validated
public class IngredientController {
    public static final String INGREDIENT_REST_POINT = "/api/ingredient";
    public static final String INGREDIENT_BY_ID_POINT = "/{ingredientId}";
    public static final String ALL_INGREDIENTS_POINT = "/get-all";
    public static final String NEW_INGREDIENT_POINT = "/new";
    public static final String EDIT_INGREDIENT_POINT = "/edit";
    public static final String DELETE_INGREDIENT_POINT = "/delete";

    private final IngredientService ingredientService;

    @GetMapping(INGREDIENT_BY_ID_POINT)
    public ResponseEntity<IngredientDto> getIngredient(@PathVariable UUID ingredientId) {
        log.info(String.format("GET %s%s: Получение ингредиента по id = %s", INGREDIENT_REST_POINT, INGREDIENT_BY_ID_POINT, ingredientId));
        return ResponseEntity.ok(ingredientService.get(ingredientId));
    }

    @GetMapping(ALL_INGREDIENTS_POINT)
    public ResponseEntity<IngredientListDto> getAllIngredients() {
        log.info(String.format("GET %s%s: Получение всех ингредиентов", INGREDIENT_REST_POINT, ALL_INGREDIENTS_POINT));
        return ResponseEntity.ok(ingredientService.getAll());
    }

    @PostMapping(NEW_INGREDIENT_POINT)
    public ResponseEntity<IngredientListDto> addIngredient(@Valid @RequestBody NewIngredientDto newIngredient) {
        log.info(String.format("POST %s%s: Добавление нового ингредиента с name = %s", INGREDIENT_REST_POINT, NEW_INGREDIENT_POINT, newIngredient.getName()));
        return ResponseEntity.ok(ingredientService.add(newIngredient));
    }

    @PostMapping(EDIT_INGREDIENT_POINT)
    public ResponseEntity<IngredientDto> updateIngredient(@Valid @RequestBody IngredientDto newIngredient) {
        log.info(String.format("POST %s%s: Изменение данных об ингредиенте с id = %s", INGREDIENT_REST_POINT, EDIT_INGREDIENT_POINT, newIngredient.getId()));
        return ResponseEntity.ok(ingredientService.edit(newIngredient));
    }

    @DeleteMapping(DELETE_INGREDIENT_POINT)
    public ResponseEntity<String> deleteIngredient(@PathVariable UUID ingredientId) {
        log.info(String.format("DELETE %s%s: Удаление блюда с id = %s", INGREDIENT_REST_POINT, DELETE_INGREDIENT_POINT, ingredientId));
        ingredientService.delete(ingredientId);
        return ResponseEntity.ok("Deleted successfully");
    }

}
