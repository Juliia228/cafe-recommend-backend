ALTER TABLE blues.users
ADD loyalty_discount_increment_count INTEGER NOT NULL,
ALTER COLUMN loyalty_discount TYPE DOUBLE PRECISION;

COMMENT ON COLUMN blues.users.loyalty_discount_increment_count
    IS 'Количество повышений персональной скидки пользователя';