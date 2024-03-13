INSERT INTO roles (id, name) VALUES (1, 'ROLE_USER');
INSERT INTO roles (id, name) VALUES (2, 'ROLE_SUPERADMIN');
INSERT INTO roles (id, name) VALUES (3, 'ROLE_ADMIN');
INSERT INTO roles (id, name) VALUES (4, 'ROLE_BUTCHER');
INSERT INTO roles (id, name) VALUES (5, 'ROLE_SUPPORT');
INSERT INTO roles (id, name) VALUES (6, 'ROLE_COURIER');

INSERT INTO users (id, name, phone, avatar, telegram_chat_id, confirmed) VALUES (1, 'Superadmin', 'superadmin@hiba.kz', null, null, 1);

INSERT INTO user_roles (user_id, role_id) VALUES (1, 2);

INSERT INTO countries (id, name) VALUES (1, 'Kazakhstan');
-- другие страны –

INSERT INTO regions (id, name, country_id) VALUES (1, 'Akmola-region', 1);
INSERT INTO regions (id, name, country_id) VALUES (2, 'Almaty-region', 1);
-- другие области --

INSERT INTO cities (id, name, region_id, country_id) VALUES (1, 'Astana', NULL, 1);
INSERT INTO cities (id, name, region_id, country_id) VALUES (2, 'Almaty', NULL, 1);
INSERT INTO cities (id, name, region_id, country_id) VALUES (3, 'Shymkent', NULL, 1);
INSERT INTO cities (id, name, region_id, country_id) VALUES (4, 'Taldykorgan', 2, NULL);

INSERT INTO categories(id, name, parent_category_id) VALUES
                                                       (1, 'horse', NULL),
                                                       (2, 'cow', NULL),
                                                       (3, 'sheep', NULL),
                                                       (4, 'birds', NULL);

-- подкатегории говядины --
INSERT INTO categories(id, name, parent_category_id) VALUES
                                                       (5, 'chuck', 2),  -- грудинка --
                                                       (6, 'rib', 2),  -- ребро--
                                                       (7, 'loin', 2),  -- ляжка --
                                                       (8, 'round', 2),  -- задняя часть--
                                                       (9, 'flank', 2),  -- бока--
                                                       (10, 'plate', 2),  -- брюшная часть--
                                                       (11, 'brisket', 2),  -- грудка --
                                                       (12, 'shank', 2);  -- голень --