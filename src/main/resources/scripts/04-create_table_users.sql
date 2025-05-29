CREATE TABLE blues.users
(
    id uuid PRIMARY KEY,
    first_name character varying(20) NOT NULL,
    last_name character varying(20),
    phone character varying(20) UNIQUE NOT NULL,
    password character varying NOT NULL,
    loyalty_discount integer,
    created_at timestamp with time zone DEFAULT now()
);

COMMENT ON TABLE blues.users
    IS 'Пользователи';
COMMENT ON COLUMN blues.users.id
    IS 'Идентификатор пользователя';
COMMENT ON COLUMN blues.users.first_name
    IS 'Имя';
COMMENT ON COLUMN blues.users.last_name
    IS 'Фамилия';
COMMENT ON COLUMN blues.users.phone
    IS 'Номер телефона';
COMMENT ON COLUMN blues.users.password
    IS 'Зашифрованный пароль';
COMMENT ON COLUMN blues.users.loyalty_discount
    IS 'Размер персональной скидки';
COMMENT ON COLUMN blues.users.created_at
    IS 'Дата, время регистрации';