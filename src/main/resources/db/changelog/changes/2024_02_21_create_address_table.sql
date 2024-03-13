create table address (
                         id INT AUTO_INCREMENT PRIMARY KEY,
                         building_name VARCHAR(255),
                         entrance VARCHAR(255),
                         apartment VARCHAR(255),
                         floor VARCHAR(255),
                         user_id INT,
                         city_id INT,
                         FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
                         FOREIGN KEY (city_id) REFERENCES cities(id) ON DELETE CASCADE
)

