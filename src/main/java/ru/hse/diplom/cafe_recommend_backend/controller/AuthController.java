package ru.hse.diplom.cafe_recommend_backend.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.hse.diplom.cafe_recommend_backend.model.dto.*;
import ru.hse.diplom.cafe_recommend_backend.service.TokenGenerationService;
import ru.hse.diplom.cafe_recommend_backend.service.UserService;

import java.util.UUID;

import static ru.hse.diplom.cafe_recommend_backend.controller.AuthController.AUTH_POINT;

@CrossOrigin
@RestController
@RequestMapping(AUTH_POINT)
@RequiredArgsConstructor
@Slf4j
@Validated
public class AuthController {
    public static final String AUTH_POINT = "/api/auth";
    public static final String REGISTER_POINT = "/register";
    public static final String LOG_IN_POINT = "/log-in";
    public static final String LOG_IN_ADMIN_POINT = LOG_IN_POINT + "/admin";
    public static final String RESET_PASSWORD_POINT = "/reset-password";
    public static final String SET_ADMIN_POINT = "/set-admin";
    public static final String REFRESH_TOKEN_POINT = "/refresh-token";

    private final UserService userService;
    private final TokenGenerationService tokenGenerationService;

    @PostMapping(REGISTER_POINT)
    public ResponseEntity<UserDto> register(@Valid @RequestBody NewUserRequestDto new_user) {
        log.info(String.format("POST %s%s: Регистрация нового пользователя", AUTH_POINT, REGISTER_POINT));
        return ResponseEntity.ok(userService.register(new_user));
    }

    @PostMapping(LOG_IN_POINT)
    public ResponseEntity<AuthResponseDto> login(@Valid @RequestBody AuthRequestDto request) {
        String login = request.getLogin();
        log.info(String.format("POST %s%s: Аутентификация пользователя login = %s", AUTH_POINT, LOG_IN_POINT, login));
        return ResponseEntity.ok(userService.authenticate(login, request.getPassword()));
    }

    @PostMapping(LOG_IN_ADMIN_POINT)
    public ResponseEntity<AuthResponseDto> loginAdmin(@Valid @RequestBody AuthRequestDto request) {
        String login = request.getLogin();
        log.info(String.format("POST %s%s: Аутентификация администратора login = %s", AUTH_POINT, LOG_IN_ADMIN_POINT, login));
        return ResponseEntity.ok(userService.authenticateAdmin(login, request.getPassword()));
    }

    @PostMapping(RESET_PASSWORD_POINT)
    public ResponseEntity<String> resetPassword(@RequestBody ResetPasswordRequestDto request) {
        log.info(String.format("POST %s%s: Сброс пароля для пользователя с phone = %s", AUTH_POINT, RESET_PASSWORD_POINT, request.getPhone()));
        userService.resetPassword(request);
        return ResponseEntity.ok("The password has been updated");
    }

    @PostMapping(REFRESH_TOKEN_POINT)
    public ResponseEntity<TokenResponseDto> refreshToken(@Valid @RequestBody RefreshTokenRequestDto request) {
        log.info(String.format("POST %s%s: Обновление access токена для пользователя с id = %s", AUTH_POINT, REFRESH_TOKEN_POINT, request.getUserId()));
        return ResponseEntity.ok(tokenGenerationService.generateAccessToken(request.getToken()));
    }

    @PostMapping(SET_ADMIN_POINT)
    public ResponseEntity<UserDto> setAdminForUser(@RequestParam UUID id) {
        log.info(String.format("POST %s%s: Назначение пользователю с id = %s роль администратора", AUTH_POINT, SET_ADMIN_POINT, id));
        return ResponseEntity.ok(userService.setRoleAdmin(id));
    }

}
