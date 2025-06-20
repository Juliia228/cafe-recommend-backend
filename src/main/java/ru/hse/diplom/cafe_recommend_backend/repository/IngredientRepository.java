package ru.hse.diplom.cafe_recommend_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.hse.diplom.cafe_recommend_backend.model.entity.Ingredient;

import java.util.List;
import java.util.UUID;

@Repository
public interface IngredientRepository extends JpaRepository<Ingredient, UUID> {

    @Query(value = """
            SELECT i.* 
            FROM blues.ingredients i 
            JOIN blues.ref_dishes_ingredients ref 
            ON ref.ingredient_id = i.id 
            JOIN blues.dishes d 
            ON ref.dish_id = d.id 
            WHERE d.id = :dishId
            """, nativeQuery = true)
    List<Ingredient> findByDishId(UUID dishId);

    @Query(value = """
            SELECT DISTINCT i.id 
            FROM blues.ingredients i 
            JOIN blues.ref_dishes_ingredients ref 
            ON ref.ingredient_id = i.id 
            JOIN blues.dishes d 
            ON ref.dish_id = d.id 
            WHERE d.id in 
                (SELECT oi.dishId 
                FROM blues.order_info oi 
                JOIN blues.orders o 
                ON o.id = oi.orderId 
                WHERE o.userId = :userId)
            """, nativeQuery = true)
    List<UUID> findDistinctOrderedIngredientIds(UUID userId);

    @Query("""
            SELECT id 
            FROM Ingredient 
            ORDER BY id
            """)
    List<UUID> findAllIds();

    @Query("""
            SELECT i 
            FROM Ingredient i 
            ORDER BY i.name
            """)
    List<Ingredient> findAllByNameOrder();

}
