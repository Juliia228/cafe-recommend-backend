ALTER TABLE blues.users
ADD roles CHARACTER VARYING(20)[] NOT NULL,
ADD key_word character varying NOT NULL;

COMMENT ON COLUMN blues.users.roles
    IS 'Роли пользователя';
COMMENT ON COLUMN blues.users.key_word
    IS 'Кодовое слово для восстановления пароля';