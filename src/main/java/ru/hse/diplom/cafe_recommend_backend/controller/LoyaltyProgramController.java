package ru.hse.diplom.cafe_recommend_backend.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.hse.diplom.cafe_recommend_backend.model.dto.LoyaltyProgramSettingsDto;
import ru.hse.diplom.cafe_recommend_backend.service.LoyaltyProgramService;

import static ru.hse.diplom.cafe_recommend_backend.controller.LoyaltyProgramController.LOYALTY_PROGRAM_POINT;

@CrossOrigin
@RequestMapping(LOYALTY_PROGRAM_POINT)
@RequiredArgsConstructor
@RestController
@Slf4j
@Validated
public class LoyaltyProgramController {
    public static final String LOYALTY_PROGRAM_POINT = "/api/loyalty-program";
    public static final String LOYALTY_SETTINGS_POINT = "/settings";
    public static final String LOYALTY_SETTINGS_UPDATE_POINT = LOYALTY_SETTINGS_POINT + "/update";

    private final LoyaltyProgramService loyaltyProgramService;

    @GetMapping(LOYALTY_SETTINGS_POINT)
    public ResponseEntity<LoyaltyProgramSettingsDto> getLoyaltySettings() {
        log.info(String.format("GET %s%s: Получение настроек программы лояльности", LOYALTY_PROGRAM_POINT, LOYALTY_SETTINGS_POINT));
        return ResponseEntity.ok(loyaltyProgramService.getLoyaltySettings());
    }

    @PostMapping(LOYALTY_SETTINGS_UPDATE_POINT)
    public ResponseEntity<LoyaltyProgramSettingsDto> updateLoyaltySettings(@RequestBody @Valid LoyaltyProgramSettingsDto settingsDto) {
        log.info(String.format("POST %s%s: Обновление настроек программы лояльности", LOYALTY_PROGRAM_POINT, LOYALTY_SETTINGS_UPDATE_POINT));
        return ResponseEntity.ok(loyaltyProgramService.updateLoyaltySettings(settingsDto));
    }

}
