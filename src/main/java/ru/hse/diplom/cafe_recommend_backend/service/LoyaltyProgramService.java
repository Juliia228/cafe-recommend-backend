package ru.hse.diplom.cafe_recommend_backend.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.hse.diplom.cafe_recommend_backend.model.dto.LoyaltyProgramSettingsDto;
import ru.hse.diplom.cafe_recommend_backend.model.entity.LoyaltyProgramSettings;
import ru.hse.diplom.cafe_recommend_backend.model.entity.User;
import ru.hse.diplom.cafe_recommend_backend.repository.LoyaltyProgramSettingsRepository;
import ru.hse.diplom.cafe_recommend_backend.repository.UserRepository;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Slf4j
public class LoyaltyProgramService {
    private final LoyaltyProgramSettingsRepository loyaltyProgramSettingsRepository;
    private final UserRepository userRepository;

    public LoyaltyProgramSettingsDto getLoyaltySettings() {
        return map(loyaltyProgramSettingsRepository.findById(1)
                .orElseThrow(() -> new RuntimeException("Настройки программы лояльности не заданы. Вызовите метод /api/loyalty-program/settings/update")));
    }

    @Transactional
    public LoyaltyProgramSettingsDto updateLoyaltySettings(LoyaltyProgramSettingsDto dto) {
        Optional<LoyaltyProgramSettings> oldSettings = loyaltyProgramSettingsRepository.findById(1);
        if (oldSettings.isPresent()) {
            double oldBaseDiscount = oldSettings.get().getBaseDiscount();
            double newBaseDiscount = dto.getBaseDiscount();
            if (oldBaseDiscount != newBaseDiscount) {
                correctBaseDiscount(newBaseDiscount);
            }

            double oldMaxDiscount = oldSettings.get().getMaxDiscount();
            double newMaxDiscount = dto.getMaxDiscount();
            if (oldMaxDiscount != newMaxDiscount) {
                correctMaxDiscount(newMaxDiscount);
            }
        }
        dto.setUpdatedAt(OffsetDateTime.now());
        return map(loyaltyProgramSettingsRepository.save(map(dto)));
    }

    @Transactional
    private void correctBaseDiscount(double newBaseDiscount) {
        List<User> allUsers = userRepository.findAll();
        allUsers = allUsers.stream()
                .map(user -> {
                    if (user.getLoyaltyDiscount() < newBaseDiscount) {
                        user.setLoyaltyDiscount(newBaseDiscount);
                    }
                    return user;
                })
                .toList();
        userRepository.saveAll(allUsers);
        log.info(String.format("Успешно обновлены loyaltyDiscount всех пользователей в соответствии с новым baseDiscount: %s", newBaseDiscount));
    }

    private void correctMaxDiscount(double newMaxDiscount) {
        List<User> allUsers = userRepository.findAll();
        allUsers = allUsers.stream()
                .map(user -> {
                    if (user.getLoyaltyDiscount() > newMaxDiscount) {
                        user.setLoyaltyDiscount(newMaxDiscount);
                    }
                    return user;
                })
                .toList();
        userRepository.saveAll(allUsers);
        log.info(String.format("Успешно обновлены loyaltyDiscount всех пользователей в соответствии с новым maxDiscount: %s", newMaxDiscount));
    }

    public static LoyaltyProgramSettingsDto map(LoyaltyProgramSettings settings) {
        return LoyaltyProgramSettingsDto.builder()
                .baseDiscount(settings.getBaseDiscount())
                .ordersThreshold(settings.getOrdersThreshold())
                .discountIncrement(settings.getDiscountIncrement())
                .maxDiscount(settings.getMaxDiscount())
                .updatedAt(settings.getUpdatedAt())
                .build();
    }

    public static LoyaltyProgramSettings map(LoyaltyProgramSettingsDto settings) {
        return LoyaltyProgramSettings.builder()
                .id(1)
                .baseDiscount(settings.getBaseDiscount())
                .ordersThreshold(settings.getOrdersThreshold())
                .discountIncrement(settings.getDiscountIncrement())
                .maxDiscount(settings.getMaxDiscount())
                .updatedAt(settings.getUpdatedAt())
                .build();
    }

}
