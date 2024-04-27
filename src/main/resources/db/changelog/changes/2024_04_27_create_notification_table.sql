CREATE TABLE `notification` (
                                `id` BIGINT NOT NULL AUTO_INCREMENT,
                                `message` TEXT NOT NULL,
                                `time` TIMESTAMP(6) NOT NULL,
                                `notification_category` VARCHAR(255) NOT NULL,
                                `is_readed` BOOLEAN NOT NULL DEFAULT FALSE,
                                `user_id` INT NOT NULL,
                                PRIMARY KEY (`id`),
                                INDEX `idx_notification_user_id` (`user_id`),
                                CONSTRAINT `fk_notification_user`
                                    FOREIGN KEY (`user_id`)
                                        REFERENCES `users` (`id`)
                                        ON DELETE CASCADE
)

