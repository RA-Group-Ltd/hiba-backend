CREATE TABLE confirmation_codes (
                                    id INT AUTO_INCREMENT PRIMARY KEY,
                                    token VARCHAR(255) NOT NULL,
                                    user_id INT NOT NULL,
                                    expiration_date TIMESTAMP NOT NULL,
                                    FOREIGN KEY (user_id) REFERENCES users (id)
);