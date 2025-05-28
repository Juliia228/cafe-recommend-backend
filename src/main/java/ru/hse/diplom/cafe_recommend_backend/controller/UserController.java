package ru.hse.diplom.cafe_recommend_backend.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.hse.diplom.cafe_recommend_backend.model.dto.GetRecommendationsRequestDto;
import ru.hse.diplom.cafe_recommend_backend.model.dto.GetUserDiscountDto;
import ru.hse.diplom.cafe_recommend_backend.model.dto.RecommendationsResponseDto;
import ru.hse.diplom.cafe_recommend_backend.model.dto.UserDto;
import ru.hse.diplom.cafe_recommend_backend.service.RecommendationService;
import ru.hse.diplom.cafe_recommend_backend.service.UserService;

import java.util.UUID;

import static ru.hse.diplom.cafe_recommend_backend.controller.UserController.USER_REST_POINT;
import static ru.hse.diplom.cafe_recommend_backend.model.Constants.PHONE_REGEXP;

@CrossOrigin
@RestController
@RequestMapping(USER_REST_POINT)
@RequiredArgsConstructor
@Slf4j
@Validated
public class UserController {
    public static final String USER_REST_POINT = "/api/user";
    public static final String GET_USER_DISCOUNT_POINT = "/discount";
    public static final String EDIT_USER_POINT = "/edit";
    public static final String DELETE_USER_POINT = "/delete";
    public static final String GET_RECOMMENDATIONS_POINT = "/recommendations";

    private final UserService userService;
    private final RecommendationService recommendationService;

    @GetMapping()
    public ResponseEntity<UserDto> getCurrentUser() {
        log.info(String.format("GET %s: Получение информации о пользователе", USER_REST_POINT));
        return ResponseEntity.ok(userService.getCurrentUser());
    }

    @GetMapping(GET_USER_DISCOUNT_POINT)
    public ResponseEntity<GetUserDiscountDto> getUserDiscount(@RequestParam @Pattern(regexp = PHONE_REGEXP) String phone) {
        log.info(String.format("GET %s%s: Получение размера скидки пользователя с phone = %s", USER_REST_POINT, GET_USER_DISCOUNT_POINT, phone));
        return ResponseEntity.ok(userService.getUserDiscount(phone));
    }

    @GetMapping(GET_RECOMMENDATIONS_POINT)
    public ResponseEntity<RecommendationsResponseDto> getRecommendationsForUser(@RequestBody GetRecommendationsRequestDto request) {
        log.info(String.format("GET %s%s: Получение рекомендаций для пользователя с id = %s", USER_REST_POINT, ALL_USERS_POINT, request.getUserId()));
        return ResponseEntity.ok(recommendationService.recommend(request));
    }

    @PostMapping(EDIT_USER_POINT)
    public ResponseEntity<UserDto> updateCurrentUser(@Valid @RequestBody UserDto new_user) {
        log.info(String.format("POST %s%s: Изменение данных пользователя с id = %s", USER_REST_POINT, EDIT_USER_POINT, new_user.getId()));
        return ResponseEntity.ok(userService.edit(new_user));
    }

    @DeleteMapping(DELETE_USER_POINT)
    public ResponseEntity<String> deleteCurrentUser(@RequestParam UUID id) {
        log.info(String.format("DELETE %s%s: Удаление пользователя с id = %s", USER_REST_POINT, DELETE_USER_POINT, id));
        userService.delete(id);
        return ResponseEntity.ok("Deleted successfully");
    }

}
