package ru.hse.diplom.cafe_recommend_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.hse.diplom.cafe_recommend_backend.model.entity.Dish;
import ru.hse.diplom.cafe_recommend_backend.model.entity.Ingredient;

import java.util.List;
import java.util.UUID;

@Repository
public interface DishRepository extends JpaRepository<Dish, UUID> {
    @Query("SELECT COUNT(*) FROM Dish")
    int getDishesCount();

//    @Query("")
//    List<Dish> findPopular();

    @Query(value = "SELECT i.* FROM ingredients i " +
            "JOIN ref_dishes_ingredients ref " +
            "ON i.id = ref.ingredient_id " +
            "WHERE ref.dish_id = ?1", nativeQuery = true)
    List<Ingredient> findIngredientsByDishId(UUID dishId);

    @Query("SELECT d FROM Dish d " +
            "WHERE d.id IN ?1 " +
            "AND d.enabled IS TRUE")
    List<Dish> findByIds(List<UUID> ids);

//    @Query("")
//    List<> findDishesWithIngredientsByIds(List<UUID> ids);

    //    @Query("")
//    List<> findAllDishesWithIngredients();

}
