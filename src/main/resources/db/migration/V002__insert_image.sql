DROP TABLE IF EXISTS image;
CREATE TABLE image
(
    id       BIGSERIAL PRIMARY KEY,
    url      VARCHAR NOT NULL,
    alt_text VARCHAR
);

ALTER TABLE "user"
    ADD COLUMN image_id BIGINT,
    ADD CONSTRAINT user_image_id_fkey FOREIGN KEY (image_id) REFERENCES image (id);

DROP TABLE IF EXISTS ticket_image;
CREATE TABLE ticket_image
(
    ticket_id BIGINT REFERENCES ticket (id),
    image_id  BIGINT REFERENCES image (id),
    PRIMARY KEY (ticket_id, image_id)
);