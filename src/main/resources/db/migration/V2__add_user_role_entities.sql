CREATE TABLE roles
(
    id serial PRIMARY KEY,
    name character varying(255) NOT NULL
);

CREATE TABLE users
(
    uuid        uuid PRIMARY KEY,
    is_active   boolean                NOT NULL,
    password    character varying(255) NOT NULL,
    username    character varying(255) NOT NULL,
    full_name   character varying(255)
);

CREATE TABLE user_role
(
    user_id uuid NOT NULL references users (uuid),
    role_id   integer NOT NULL references roles (id)
);

CREATE INDEX users_uuid ON users (uuid);
CREATE UNIQUE INDEX users_usernames ON users (username);
