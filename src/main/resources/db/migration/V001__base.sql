DROP TYPE IF EXISTS status;
CREATE TYPE status AS ENUM ('open', 'assign','done');

DROP TYPE IF EXISTS tag;
CREATE TYPE tag AS ENUM ('sanitary', 'wood', 'metal', 'electric', 'moving', 'painter', 'renovation', 'gardening', 'montage');

DROP TABLE IF EXISTS "user";
CREATE TABLE "user"
(
    id                      BIGSERIAL PRIMARY KEY,
    subject                 VARCHAR(256) NOT NULL,
    username                VARCHAR(256),
    description             TEXT,
    verified                BOOLEAN,
    user_create_date        TIMESTAMP,
    user_last_modified_date TIMESTAMP
);

DROP TABLE IF EXISTS ticket;
CREATE TABLE ticket
(
    id          BIGSERIAL PRIMARY KEY,
    title       VARCHAR                       NOT NULL,
    description TEXT                          NOT NULL,
    status      STATUS                        NOT NULL,
    created     TIMESTAMP                     NOT NULL,
    user_id     BIGINT REFERENCES "user" (id) NOT NULL,
    assigned_to BIGINT REFERENCES "user" (id)
);

DROP TABLE IF EXISTS user_tag;
CREATE TABLE user_tag
(
    user_id BIGINT REFERENCES "user" (id) NOT NULL,
    tag     tag                           NOT NULL,
    PRIMARY KEY (user_id, tag)
);

DROP TABLE IF EXISTS ticket_tag;
CREATE TABLE ticket_tag
(
    ticket_id BIGINT REFERENCES ticket (id) NOT NULL,
    tag       tag                           NOT NULL,
    PRIMARY KEY (ticket_id, tag)
);
