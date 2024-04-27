CREATE TABLE `chat_message` (
                                `id` BIGINT NOT NULL AUTO_INCREMENT,
                                `chat_id` BIGINT NOT NULL,
                                `content` TEXT,
                                `timestamp` DATETIME NOT NULL,
                                `recipient_type` VARCHAR(255) NOT NULL,
                                `status` VARCHAR(255) NOT NULL,
                                PRIMARY KEY (`id`),
                                INDEX `idx_chat_message_chat_id` (`chat_id`),
                                CONSTRAINT `fk_chat_message_chat_id`
                                    FOREIGN KEY (`chat_id`)
                                        REFERENCES `chat` (`id`)
                                        ON DELETE CASCADE
)

