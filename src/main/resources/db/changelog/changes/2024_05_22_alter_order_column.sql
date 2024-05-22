ALTER TABLE orders
    ADD COLUMN courier_id BIGINT NOT NULL;

ALTER TABLE orders
    ADD CONSTRAINT fk_courier_id
        FOREIGN KEY (courier_id) REFERENCES couriers(id)
            ON DELETE CASCADE;