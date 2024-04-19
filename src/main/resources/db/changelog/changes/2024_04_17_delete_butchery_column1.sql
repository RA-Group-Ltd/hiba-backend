alter  table butcheries
    drop constraint fk_country_id;

alter table butcheries
    drop constraint fk_region_id;

ALTER TABLE butcheries
    drop column countryId;

Alter TABLE butcheries
    drop column regionId;


