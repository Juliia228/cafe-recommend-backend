package ru.hse.diplom.cafe_recommend_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.hse.diplom.cafe_recommend_backend.model.dto.DishPopularityDto;
import ru.hse.diplom.cafe_recommend_backend.model.entity.Dish;
import ru.hse.diplom.cafe_recommend_backend.model.entity.Ingredient;

import java.util.List;
import java.util.UUID;

@Repository
public interface DishRepository extends JpaRepository<Dish, UUID> {
    @Query("SELECT COUNT(*) FROM Dish")
    Integer getDishesCount();

    @Query("""
            SELECT id 
            FROM Dish 
            ORDER BY id
            """)
    List<UUID> findAllIds();

    @Query("""
            SELECT new ru.hse.diplom.cafe_recommend_backend.model.dto.DishPopularityDto(d, SUM(oi.dishCount)) 
            FROM OrderInfo oi 
            JOIN Dish d 
            ON oi.dishId = d.id 
            GROUP BY d
            ORDER BY SUM(oi.dishCount) DESC 
            """)
    List<DishPopularityDto> findPopular();

    @Query(value = """
            SELECT i.* FROM ingredients i 
            JOIN ref_dishes_ingredients ref 
            ON i.id = ref.ingredient_id 
            WHERE ref.dish_id = :dishId
            """, nativeQuery = true)
    List<Ingredient> findIngredientsByDishId(UUID dishId);

    @Query("""
            SELECT d FROM Dish d 
            WHERE d.id IN :ids 
            AND d.enabled IS TRUE
            """)
    List<Dish> findByIds(List<UUID> ids);

//    @Query("")
//    List<> findDishesWithIngredientsByIds(List<UUID> ids);

    //    @Query("")
//    List<> findAllDishesWithIngredients();

}
