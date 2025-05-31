CREATE INDEX idx__users__phone ON blues.users (phone);

CREATE INDEX idx__orders__user ON blues.orders (user_id);
CREATE INDEX idx__orders__user_created_at ON blues.orders (user_id, created_at);

CREATE INDEX idx__order_info__order ON blues.order_info (order_id);
CREATE INDEX idx__order_info__dish ON blues.order_info (dish_id);
CREATE INDEX idx__order_info__order_dish ON blues.order_info (order_id, dish_id);

CREATE INDEX idx__tokens__user_id ON blues.tokens (user_id);

