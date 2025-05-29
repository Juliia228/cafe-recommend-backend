package ru.hse.diplom.cafe_recommend_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.hse.diplom.cafe_recommend_backend.model.entity.LoyaltyProgramSettings;

public interface LoyaltyProgramSettingsRepository extends JpaRepository<LoyaltyProgramSettings, Integer> {

}
