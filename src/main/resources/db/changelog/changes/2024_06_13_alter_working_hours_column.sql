ALTER TABLE working_hours
    CHANGE COLUMN open_time open_time VARCHAR(5) DEFAULT '00:00',
    CHANGE COLUMN close_time close_time VARCHAR(5) DEFAULT '00:00';
