CREATE TABLE blues.order_info
(
    id uuid PRIMARY KEY,
    order_id uuid NOT NULL,
    dish_id uuid NOT NULL REFERENCES blues.dishes (id),
    dish_count integer NOT NULL DEFAULT 1,
    FOREIGN KEY (order_id)
        REFERENCES blues.orders (id)
        ON DELETE CASCADE
);

COMMENT ON TABLE blues.order_info
    IS 'Детали заказов';
COMMENT ON COLUMN blues.order_info.id
    IS 'Идентификатор';
COMMENT ON COLUMN blues.order_info.order_id
    IS 'Идентификатор заказа';
COMMENT ON COLUMN blues.order_info.dish_id
    IS 'Идентификатор блюда';
COMMENT ON COLUMN blues.order_info.dish_count
    IS 'Количество блюд (по dish_id)';