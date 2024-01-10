CREATE TABLE roles
(
    id   SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

CREATE TABLE users
(
    id        SERIAL PRIMARY KEY,
    role_id   BIGINT,
    e_mail    VARCHAR(255) NOT NULL,
    full_name VARCHAR(255) NOT NULL,
    password  VARCHAR(255) NOT NULL,
    notification_type  VARCHAR(255) NOT NULL,
    telegram  VARCHAR(255) NOT NULL
);

ALTER TABLE roles
    ADD CONSTRAINT unique_name UNIQUE (name);

ALTER TABLE users
    ADD CONSTRAINT unique_email UNIQUE (e_mail);

