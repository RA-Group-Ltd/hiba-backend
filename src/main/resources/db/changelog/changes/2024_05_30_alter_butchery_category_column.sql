# ALTER TABLE butchery_category DROP COLUMN IF EXISTS category_id;
ALTER TABLE butchery_category ADD COLUMN IF NOT EXISTS category_id INT;
ALTER TABLE butchery_category add FOREIGN KEY (category_id) REFERENCES categories(id) ON DELETE CASCADE;
