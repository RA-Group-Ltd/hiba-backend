alter table working_hours DROP CONSTRAINT  working_hours_ibfk_1;
ALTER TABLE working_hours add FOREIGN KEY (butcher_id) REFERENCES butcheries(id) ON DELETE CASCADE;
