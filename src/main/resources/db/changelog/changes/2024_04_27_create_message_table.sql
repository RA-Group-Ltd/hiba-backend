CREATE TABLE `message` (
                           `id` BIGINT NOT NULL AUTO_INCREMENT,
                           `chat_id` BIGINT NOT NULL,
                           `sender_id` INT,
                           `content` TEXT,
                           `images` LONGBLOB,
                           `created_at` TIMESTAMP(6) NOT NULL,
                           PRIMARY KEY (`id`),
                           INDEX `idx_message_chat_id` (`chat_id`),
                           INDEX `idx_message_sender_id` (`sender_id`),
                           CONSTRAINT `fk_message_chat`
                               FOREIGN KEY (`chat_id`)
                                   REFERENCES `chat` (`id`)
                                   ON DELETE CASCADE,
                           CONSTRAINT `fk_message_sender`
                               FOREIGN KEY (`sender_id`)
                                   REFERENCES `users` (`id`)
                                   ON DELETE SET NULL
);

