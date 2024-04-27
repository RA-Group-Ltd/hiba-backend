CREATE TABLE `chat_notification` (
                                     `id` BIGINT NOT NULL AUTO_INCREMENT,
                                     `message_id` BIGINT NOT NULL,
                                     `sender_id` BIGINT NOT NULL,
                                     `sender_name` VARCHAR(255) NOT NULL,
                                     PRIMARY KEY (`id`),
                                     INDEX `idx_chat_notification_message_id` (`message_id`),
                                     INDEX `idx_chat_notification_sender_id` (`sender_id`)
);

