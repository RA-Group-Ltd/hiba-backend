create table working_hours (
                        id BIGINT AUTO_INCREMENT PRIMARY KEY,
                        day_of_week VARCHAR(255),
                        is_closed Boolean,
                        open_time TIMESTAMP,
                        close_time TIMESTAMP,
                        butcher_id INT,
                        FOREIGN KEY (butcher_id) REFERENCES butchers(id) ON DELETE CASCADE
)