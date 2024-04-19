ALTER TABLE butcheries
    drop column owner;

Alter TABLE butcheries
    drop column phone;

ALTER TABLE butcheries
    ADD COLUMN owner INT;

ALTER TABLE butcheries
    ADD CONSTRAINT fk_owner
        FOREIGN KEY (owner) REFERENCES users(id) ON DELETE CASCADE;

