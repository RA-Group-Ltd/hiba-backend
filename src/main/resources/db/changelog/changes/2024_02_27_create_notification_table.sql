CREATE TABLE notification (
                              id BIGINT PRIMARY KEY AUTO_INCREMENT,
                              message VARCHAR(255),
                              time DATETIME,
                              notification_category VARCHAR(255),
                              user_id BIGINT,
                              FOREIGN KEY (user_id) REFERENCES users(id)
);