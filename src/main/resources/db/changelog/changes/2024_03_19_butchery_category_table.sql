CREATE TABLE butchery_category (
                                   id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                   butchery_id int,
                                   category_id int,
                                   FOREIGN KEY (butchery_id) REFERENCES butcheries(id) ON DELETE CASCADE ,
                                   FOREIGN KEY (category_id) REFERENCES categories(id) ON DELETE CASCADE
);