CREATE TABLE users  (
                        id INT AUTO_INCREMENT PRIMARY KEY,
                        name VARCHAR(255) NULL,
                        phone VARCHAR(255) NOT NULL UNIQUE,
                        avatar LONGBLOB NULL,
                        telegram_chat_id VARCHAR(255) NULL,
                        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                        confirmed TINYINT(1) DEFAULT 0 -- добавлен новый столбец
);

CREATE TABLE roles (
                       id INT AUTO_INCREMENT PRIMARY KEY,
                       name VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE user_roles (
                            user_id INT,
                            role_id INT,
                            FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
                            FOREIGN KEY (role_id) REFERENCES roles(id) ON DELETE CASCADE
);

CREATE TABLE verification_codes (
                                    id INT AUTO_INCREMENT PRIMARY KEY,
                                    token VARCHAR(255) NOT NULL,
                                    user_id INT NOT NULL,
                                    expiration_date TIMESTAMP NOT NULL,
                                    FOREIGN KEY (user_id) REFERENCES users (id)
);

CREATE TABLE countries (
                           id INT AUTO_INCREMENT PRIMARY KEY,
                           name VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE regions (
                         id INT AUTO_INCREMENT PRIMARY KEY,
                         name VARCHAR(50) NOT NULL,
                         country_id INT,
                         FOREIGN KEY (country_id) REFERENCES countries(id) ON DELETE CASCADE
);

CREATE TABLE cities(
                       id INT AUTO_INCREMENT PRIMARY KEY,
                       name VARCHAR(255) NOT NULL,
                       region_id INT,
                       country_id INT,
                       FOREIGN KEY (region_id) REFERENCES regions(id) ON DELETE CASCADE,
                       FOREIGN KEY (country_id) REFERENCES countries(id) ON DELETE CASCADE
);

CREATE TABLE categories (
                            id INT AUTO_INCREMENT PRIMARY KEY,
                            name VARCHAR(255) NOT NULL,
                            parent_category_id INT,
                            FOREIGN KEY (parent_category_id) REFERENCES categories(id) ON DELETE CASCADE
);

CREATE TABLE butcheries (
                            id INT AUTO_INCREMENT PRIMARY KEY,
                            name VARCHAR(255) NOT NULL,
                            city_id INT,
                            latitude DECIMAL(9, 6) NOT NULL,
                            longitude DECIMAL(9, 6) NOT NULL,
                            address VARCHAR(255) NOT NULL,
                            rating DECIMAL(2, 1) DEFAULT 0,
                            rateSum INT DEFAULT 0,
                            rateCount INT DEFAULT 0,
                            FOREIGN KEY (city_id) REFERENCES cities(id) ON DELETE CASCADE
);

CREATE TABLE butchers (
                          id INT AUTO_INCREMENT PRIMARY KEY,
                          user_id INT,
                          butchery_id INT,
                          FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
                          FOREIGN KEY (butchery_id) REFERENCES butcheries(id) ON DELETE CASCADE
);

CREATE TABLE menu (
                      id INT AUTO_INCREMENT PRIMARY KEY,
                      name VARCHAR(255) NOT NULL,
                      butchery_id INT,
                      category_id INT,
                      weight INT,
                      isWholeAnimal BOOLEAN,
                      FOREIGN KEY (category_id) REFERENCES categories(id) ON DELETE CASCADE,
                      FOREIGN KEY (butchery_id) REFERENCES butcheries(id) ON DELETE CASCADE
);

CREATE TABLE reviews (
                         id INT AUTO_INCREMENT PRIMARY KEY,
                         rate DECIMAL(2, 1) DEFAULT 0,
                         text MEDIUMTEXT,
                         butchery_id INT,
                         user_id INT,
                         createdAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                         FOREIGN KEY (butchery_id) REFERENCES butcheries(id) ON DELETE CASCADE,
                         FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);