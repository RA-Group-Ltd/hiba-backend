CREATE TABLE butchery_document (
                                       id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                       certificate_image BLOB,
                                       butchery_id INT,
                                       CONSTRAINT fk_butchery_certificates_butchery FOREIGN KEY (butchery_id) REFERENCES butcheries(id)
);

