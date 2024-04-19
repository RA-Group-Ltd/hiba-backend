alter table butcheries
    drop column meatType;

alter table butcheries
    drop column regNumber;

alter table butcheries
    add column meat_type VARCHAR(255);

alter table butcheries
    add column reg_number VARCHAR(255);




