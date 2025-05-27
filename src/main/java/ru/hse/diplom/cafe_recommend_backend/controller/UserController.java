package ru.hse.diplom.cafe_recommend_backend.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.hse.diplom.cafe_recommend_backend.model.dto.GetRecommendationsRequestDto;
import ru.hse.diplom.cafe_recommend_backend.model.dto.RecommendationsResponseDto;
import ru.hse.diplom.cafe_recommend_backend.model.entity.User;
import ru.hse.diplom.cafe_recommend_backend.service.RecommendationService;
import ru.hse.diplom.cafe_recommend_backend.service.UserService;

import java.util.List;
import java.util.UUID;

import static ru.hse.diplom.cafe_recommend_backend.controller.UserController.USER_REST_POINT;

@CrossOrigin
@RestController
@RequestMapping(USER_REST_POINT)
@RequiredArgsConstructor
@Slf4j
@Validated
public class UserController {
    public static final String USER_REST_POINT = "/api/user";
    public static final String USER_BY_ID_POINT = "/{userId}";
    public static final String ALL_USERS_POINT = "/getAll";
    public static final String EDIT_USER_POINT = "/edit";
    public static final String DELETE_USER_POINT = "/delete";
    public static final String GET_RECOMMENDATIONS_POINT = "/recommendations";

    private final UserService userService;
    private final RecommendationService recommendationService;

    @GetMapping(USER_BY_ID_POINT)
    public ResponseEntity<User> getUser(@PathVariable
//                                            @Parameter(description = "Идентификатор пользователя")
                                            UUID userId) {
        log.info(String.format("GET %s%s: Получение пользователя по id = %s", USER_REST_POINT, USER_BY_ID_POINT, userId));
        return ResponseEntity.ok(userService.get(userId));
    }

    @GetMapping(ALL_USERS_POINT)
    public ResponseEntity<List<User>> getAllUsers() {
        log.info(String.format("GET %s%s: Получение всех пользователей", USER_REST_POINT, ALL_USERS_POINT));
        return ResponseEntity.ok(userService.getAll());
    }

    @GetMapping(GET_RECOMMENDATIONS_POINT)
    public ResponseEntity<RecommendationsResponseDto> getRecommendationsForUser(@RequestBody GetRecommendationsRequestDto request) {
        log.info(String.format("GET %s%s: Получение рекомендаций для пользователя с id = %s", USER_REST_POINT, ALL_USERS_POINT, request.getUserId()));
        return ResponseEntity.ok(recommendationService.recommend(request));
    }

    @PostMapping(EDIT_USER_POINT)
    public ResponseEntity<User> updateUser(@Valid @RequestBody User new_user) {
        log.info(String.format("POST %s%s: Изменение данных пользователя с id = %s", USER_REST_POINT, EDIT_USER_POINT, new_user.getId()));
        return ResponseEntity.ok(userService.edit(new_user));
    }

    @DeleteMapping(DELETE_USER_POINT)
    public ResponseEntity<String> deleteUser(@RequestParam UUID id) {
        log.info(String.format("DELETE %s%s: Удаление пользователя с id = %s", USER_REST_POINT, DELETE_USER_POINT, id));
        userService.delete(id);
        return ResponseEntity.ok("Deleted successfully");
    }

}
