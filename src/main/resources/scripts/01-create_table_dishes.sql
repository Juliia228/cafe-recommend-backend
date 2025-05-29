CREATE TABLE blues.dishes
(
    id uuid PRIMARY KEY,
    name character varying NOT NULL,
    description text,
    price integer NOT NULL,
    enabled boolean DEFAULT false
);

COMMENT ON TABLE blues.dishes
    IS 'Блюда кафе "Блюз"';
COMMENT ON COLUMN blues.dishes.id
    IS 'Идентификатор блюда';
COMMENT ON COLUMN blues.dishes.name
    IS 'Название блюда';
COMMENT ON COLUMN blues.dishes.description
    IS 'Описание блюда';
COMMENT ON COLUMN blues.dishes.price
    IS 'Цена блюда';
COMMENT ON COLUMN blues.dishes.enabled
    IS 'Доступно ли блюдо для заказа';