create table order_menu_items (
    order_id bigint,
    menu_items_key int,
    count int,
    constraint pk_order_menu_items primary key (order_id, menu_items_key),
    constraint fk_order_menu_items_order_id
        foreign key (order_id) references orders(id)
        on delete cascade,
    constraint fk_order_menu_items_menu_id
        foreign key (menu_items_key) references menu(id)
        on delete cascade
)


