CREATE TABLE chat (
                        id BIGINT NOT NULL AUTO_INCREMENT,
                        client_id BIGINT NOT NULL,
                        support_id BIGINT NOT NULL,
                        archive BOOLEAN NOT NULL DEFAULT FALSE,
                        rate INT NOT NULL DEFAULT 0,
                        order_id BIGINT,
                        PRIMARY KEY (id),
                        INDEX idx_chat_client_id (client_id),
                        INDEX idx_chat_support_id (support_id),
                        INDEX idx_chat_order_id (order_id),
                        CONSTRAINT fk_chat_order_id
                            FOREIGN KEY (order_id)
                                REFERENCES orders (id)
                                ON DELETE SET NULL
)

