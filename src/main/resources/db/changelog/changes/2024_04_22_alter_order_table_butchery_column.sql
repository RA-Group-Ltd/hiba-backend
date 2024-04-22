alter table orders
    drop constraint orders_ibfk_3;

alter table orders
    drop column butcher_id;

alter table orders
    add column butchery_id int;

alter table orders
    add FOREIGN KEY (butchery_id) REFERENCES butcheries(id) ON DELETE CASCADE;


