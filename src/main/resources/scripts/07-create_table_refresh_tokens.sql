CREATE TABLE blues.tokens
(
    id uuid PRIMARY KEY,
    token character varying NOT NULL,
    expiration timestamp with time zone NOT NULL,
    user_id uuid NOT NULL,
    FOREIGN KEY (user_id)
        REFERENCES blues.users (id)
        ON UPDATE NO ACTION
        ON DELETE RESTRICT
);

COMMENT ON TABLE blues.tokens
    IS 'Refresh токены';
COMMENT ON COLUMN blues.tokens.id
    IS 'Идентификатор';
COMMENT ON COLUMN blues.tokens.token
    IS 'Refresh токен';
COMMENT ON COLUMN blues.tokens.expiration
    IS 'Дата истечения срока токена';
COMMENT ON COLUMN blues.tokens.user_id
    IS 'Идентификатор пользователя';
