ALTER TABLE butcheries
    ADD COLUMN owner VARCHAR(255),
    ADD COLUMN regNumber VARCHAR(255),
    ADD COLUMN phone VARCHAR(255),
    ADD COLUMN email VARCHAR(255),
    ADD COLUMN meatType VARCHAR(255),
    ADD COLUMN regionId INT,
    ADD COLUMN countryId INT;

ALTER TABLE butcheries
    ADD CONSTRAINT fk_region_id
        FOREIGN KEY (regionId) REFERENCES regions(id) ON DELETE CASCADE;

ALTER TABLE butcheries
    ADD CONSTRAINT fk_country_id
        FOREIGN KEY (countryId) REFERENCES countries(id) ON DELETE CASCADE;
