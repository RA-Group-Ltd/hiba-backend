CREATE TABLE promotion (
                           id BIGINT AUTO_INCREMENT PRIMARY KEY,
                           title VARCHAR(255),
                           description TEXT,
                           image LONGBLOB,
                           created_at TIMESTAMP(6)
);

