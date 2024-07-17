ALTER TABLE confirmation_codes
    ADD COLUMN order_id BIGINT NOT NULL;

ALTER TABLE confirmation_codes
    ADD CONSTRAINT fk_order_id
        FOREIGN KEY (order_id) REFERENCES orders(id)
            ON DELETE CASCADE;