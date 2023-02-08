DROP TABLE IF EXISTS user_roles;
DROP TABLE IF EXISTS comments;
DROP TABLE IF EXISTS entries;
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS confirmation_token;
DROP TABLE IF EXISTS refresh_token;
DROP SEQUENCE IF EXISTS global_seq;

CREATE SEQUENCE global_seq START WITH 1;

CREATE TABLE users
(
    id              INTEGER PRIMARY KEY DEFAULT nextval('global_seq'),
    name            VARCHAR                           NOT NULL,
    email           VARCHAR                           NOT NULL,
    password        VARCHAR                           NOT NULL,
    description     VARCHAR                           NOT NULL,
    registered      DATE                DEFAULT now() NOT NULL,
    enabled         BOOL                DEFAULT TRUE  NOT NULL
);
CREATE UNIQUE INDEX user_unique_email_idx ON users (email);

CREATE TABLE user_roles
(
    user_id INTEGER NOT NULL,
    role    VARCHAR,
    CONSTRAINT user_roles_idx UNIQUE (user_id, role),
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

CREATE TABLE entries
(
    id      INTEGER PRIMARY KEY DEFAULT nextval('global_seq'),
    user_id INTEGER                           NOT NULL,
    title   VARCHAR                           NOT NULL,
    content VARCHAR                           NOT NULL,
    updated TIMESTAMP(0)        DEFAULT now() NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

CREATE TABLE comments
(
    id       INTEGER PRIMARY KEY DEFAULT nextval('global_seq'),
    user_id  INTEGER                           NOT NULL,
    entry_id INTEGER                           NOT NULL,
    text     VARCHAR                           NOT NULL,
    posted   TIMESTAMP(0)        DEFAULT now() NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    FOREIGN KEY (entry_id) REFERENCES entries (id) ON DELETE CASCADE
);

CREATE TABLE confirmation_token
(
    user_id     INTEGER                 NOT NULL,
    token       VARCHAR                 NOT NULL,
    created_at  TIMESTAMP DEFAULT now() NOT NULL,
    expiresAt   TIMESTAMP               NOT NULL,
    confirmedAt TIMESTAMP
);

CREATE TABLE refresh_token
(
    user_id     INTEGER   NOT NULL,
    token       VARCHAR   NOT NULL,
    expiry_date TIMESTAMP NOT NULL
);