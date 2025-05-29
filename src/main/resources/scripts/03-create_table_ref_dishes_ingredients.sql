CREATE TABLE blues.ref_dishes_ingredients
(
    id uuid PRIMARY KEY,
    dish_id uuid NOT NULL,
    ingredient_id uuid NOT NULL,
    FOREIGN KEY (dish_id)
        REFERENCES blues.dishes (id)
        ON DELETE CASCADE,
    FOREIGN KEY (ingredient_id)
        REFERENCES blues.ingredients (id)
        ON DELETE CASCADE
);

COMMENT ON TABLE blues.ref_dishes_ingredients
    IS 'Таблица для связи many-to-many таблиц dishes и ingredients';
COMMENT ON COLUMN blues.ref_dishes_ingredients.id
    IS 'Идентификатор';
COMMENT ON COLUMN blues.ref_dishes_ingredients.dish_id
    IS 'Идентификатор блюда';
COMMENT ON COLUMN blues.ref_dishes_ingredients.ingredient_id
    IS 'Идентификатор ингредиента';