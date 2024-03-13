create table orders (
                        id BIGINT AUTO_INCREMENT PRIMARY KEY,
                        order_status VARCHAR(255),
                        address_id INT,
                        user_id INT,
                        butcher_id INT,
                        FOREIGN KEY (address_id) REFERENCES address(id) ON DELETE CASCADE,
                        FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
                        FOREIGN KEY (butcher_id) REFERENCES butchers(id) ON DELETE CASCADE
)