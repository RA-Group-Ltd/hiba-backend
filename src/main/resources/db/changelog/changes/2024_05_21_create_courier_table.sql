CREATE TABLE couriers (
                          id BIGINT AUTO_INCREMENT PRIMARY KEY,
                          user_id INT NOT NULL,
                          rating DOUBLE DEFAULT 0.0,
                          city_id INT,
                          created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

                          FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
                          FOREIGN KEY (city_id) REFERENCES cities(id) ON DELETE CASCADE
);