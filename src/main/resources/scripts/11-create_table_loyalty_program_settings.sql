CREATE TABLE blues.loyalty_program_settings (
    id INTEGER PRIMARY KEY DEFAULT 1,
    base_discount DOUBLE PRECISION NOT NULL,
    orders_threshold INTEGER NOT NULL,
    discount_increment DOUBLE PRECISION NOT NULL,
    max_discount DOUBLE PRECISION NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE
);

COMMENT ON TABLE blues.loyalty_program_settings IS 'Настройки программы лояльности';
COMMENT ON COLUMN blues.loyalty_program_settings.id IS 'Идентификатор (1 для единственной строки с настройками)';
COMMENT ON COLUMN blues.loyalty_program_settings.base_discount IS 'Базовый процент скидки для новых пользователей по программе лояльности';
COMMENT ON COLUMN blues.loyalty_program_settings.orders_threshold IS 'Количество заказов, необходимое для повышения скидки';
COMMENT ON COLUMN blues.loyalty_program_settings.discount_increment IS 'Процент повышения скидки, когда достигнуто количество заказов, необходимое для повышения';
COMMENT ON COLUMN blues.loyalty_program_settings.max_discount IS 'Максимально возможный процент скидки';
COMMENT ON COLUMN blues.loyalty_program_settings.updated_at IS 'Дата и время последнего обновления настроек';