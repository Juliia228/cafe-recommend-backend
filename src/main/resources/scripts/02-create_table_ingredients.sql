CREATE TABLE blues.ingredients
(
    id uuid PRIMARY KEY,
    name character varying(50) UNIQUE NOT NULL
);

COMMENT ON TABLE blues.ingredients
    IS 'Ингредиенты';
COMMENT ON COLUMN blues.ingredients.id
    IS 'Идентификатор ингредиента';
COMMENT ON COLUMN blues.ingredients.name
    IS 'Название ингредиента на русском языке';