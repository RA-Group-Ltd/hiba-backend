CREATE TABLE notification (
                              id BIGINT AUTO_INCREMENT PRIMARY KEY,
                              message VARCHAR(255),
                              time TIMESTAMP(6),
                              notification_category VARCHAR(50),
                              user_id INT,
                              FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
)