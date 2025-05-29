CREATE TABLE blues.orders
(
    id uuid PRIMARY KEY,
    user_id uuid NOT NULL REFERENCES blues.users (id),
    created_at timestamp with time zone DEFAULT now()        
);

COMMENT ON TABLE blues.orders
    IS 'История заказов';
COMMENT ON COLUMN blues.orders.id
    IS 'Идентификатор заказа';
COMMENT ON COLUMN blues.orders.user_id
    IS 'Идентификатор пользователя, сделавшего заказ';
COMMENT ON COLUMN blues.orders.created_at
    IS 'Время создания заказа';