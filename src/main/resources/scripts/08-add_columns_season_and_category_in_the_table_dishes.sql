ALTER TABLE blues.dishes
ADD category CHARACTER VARYING(50) NOT NULL,
ADD season CHARACTER VARYING(10) NOT NULL;

COMMENT ON COLUMN blues.dishes.category
    IS 'Категория блюда';
COMMENT ON COLUMN blues.dishes.season
    IS 'В какой сезон лучше рекомендовать блюдо. "DEFAULT" - блюдо подходит для любого сезона';