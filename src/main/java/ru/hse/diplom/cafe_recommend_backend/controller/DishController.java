package ru.hse.diplom.cafe_recommend_backend.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hse.diplom.cafe_recommend_backend.model.dto.*;
import ru.hse.diplom.cafe_recommend_backend.service.DishService;
import ru.hse.diplom.cafe_recommend_backend.service.IngredientService;

import java.util.UUID;

import static ru.hse.diplom.cafe_recommend_backend.controller.DishController.DISH_POINT;

@CrossOrigin
@RequestMapping(DISH_POINT)
@RequiredArgsConstructor
@RestController
@Slf4j
public class DishController {
    public static final String DISH_POINT = "/api/dish";
    public static final String GET_DISH_POINT = "/{dishId}";
    public static final String GET_DISH_WITH_INGREDIENTS_POINT = "/{dishId}/fullInfo";
    public static final String GET_ALL_DISHES_POINT = "/getAll";
    public static final String NEW_DISH_POINT = "/new";
    public static final String EDIT_DISH_POINT = "/edit";
    public static final String DELETE_DISH_POINT = "/{dishId}";
    public static final String GET_DISH_INGREDIENTS_POINT = "/{dishId}/ingredients";

    private final DishService dishService;
    private final IngredientService ingredientService;

    @GetMapping(GET_DISH_POINT)
    public ResponseEntity<DishDto> getDish(@PathVariable UUID dishId) {
        log.info(String.format("GET %s%s: Получение блюда по id = %s", DISH_POINT, GET_DISH_POINT, dishId));
        return ResponseEntity.ok(dishService.get(dishId));
    }

    @GetMapping(GET_DISH_INGREDIENTS_POINT)
    public ResponseEntity<IngredientListDto> getDishIngredients(@PathVariable UUID dishId) {
        log.info(String.format("GET %s%s: Получение ингредиентов блюда по dishId = %s", DISH_POINT, GET_DISH_INGREDIENTS_POINT, dishId));
        return ResponseEntity.ok(ingredientService.getIngredientsByDishId(dishId));
    }

    @GetMapping(GET_DISH_WITH_INGREDIENTS_POINT)
    public ResponseEntity<FullDishInfoDto> getDishWithIngredients(@PathVariable UUID dishId) {
        log.info(String.format("GET %s%s: Получение блюда с ингредиентами по id = %s", DISH_POINT, GET_DISH_WITH_INGREDIENTS_POINT, dishId));
        return ResponseEntity.ok(dishService.getWithIngredients(dishId));
    }

    @GetMapping(GET_ALL_DISHES_POINT)
    public ResponseEntity<DishListDto> getAllDishes(@RequestParam(defaultValue = "false") Boolean withIngredients) {
        log.info(String.format("GET %s%s: Получение всех блюд", DISH_POINT, GET_ALL_DISHES_POINT));
        return ResponseEntity.ok(dishService.getAll(withIngredients));
    }

    @PostMapping(NEW_DISH_POINT)
    public ResponseEntity<DishDto> addDish(@Valid @RequestBody NewDishDto new_dish) {
        log.info(String.format("POST %s%s: Добавление нового блюда с name = %s", DISH_POINT, NEW_DISH_POINT, new_dish.getName()));
        return ResponseEntity.ok(dishService.add(new_dish));
    }

    @PostMapping(EDIT_DISH_POINT)
    public ResponseEntity<DishDto> updateDish(@Valid @RequestBody DishDto new_dish) {
        log.info(String.format("POST %s%s: Изменение данных о блюде с id = %s", DISH_POINT, EDIT_DISH_POINT, new_dish.getId()));
        return ResponseEntity.ok(dishService.edit(new_dish));
    }

    @DeleteMapping(DELETE_DISH_POINT)
    public ResponseEntity<String> deleteDish(@PathVariable UUID dishId) {
        log.info(String.format("DELETE %s%s: Удаление блюда с id = %s", DISH_POINT, DELETE_DISH_POINT, dishId));
        dishService.delete(dishId);
        return ResponseEntity.ok("Deleted successfully");
    }

}
