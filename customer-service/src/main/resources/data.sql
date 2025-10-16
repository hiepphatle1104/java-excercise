CREATE TABLE IF NOT EXISTS customer
(
    id              UUID PRIMARY KEY,
    email           VARCHAR(255) UNIQUE NOT NULL,
    hashed_password VARCHAR(255)        NOT NULL,
    first_name      VARCHAR(255),
    last_name       VARCHAR(255),
    address         VARCHAR(255),
    phone_number    VARCHAR(25),
    created_at      TIMESTAMP           NOT NULL,
    updated_at      TIMESTAMP           NOT NULL
);