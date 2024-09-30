--liquibase formatted sql

--changeset krasnopolskyi:1
INSERT INTO user (first_name, last_name, username, password, is_active)
VALUES ('John', 'Doe', 'john_doe', 'root', TRUE),
       ('Jane', 'Smith', 'jane_smith', 'root', TRUE),
       ('Mike', 'Tyson', 'mike_tyson', 'root', TRUE),
       ('Usain', 'Bolt', 'usain_bolt', 'root', TRUE),
       ('Serena', 'Williams', 'serena_williams', 'root', TRUE);

--changeset krasnopolskyi:2
INSERT INTO trainee (user_id, date_of_birth, address)
VALUES ((SELECT id FROM user WHERE username = 'john_doe'), '1990-05-15', '123 Main St, City, Country'),
       ((SELECT id FROM user WHERE username = 'jane_smith'), '1985-08-22', '456 Oak St, City, Country'),
       ((SELECT id FROM user WHERE username = 'mike_tyson'), '1966-06-30', '789 Pine St, City, Country'),
       ((SELECT id FROM user WHERE username = 'usain_bolt'), '1986-08-21', '101 Maple St, City, Country'),
       ((SELECT id FROM user WHERE username = 'serena_williams'), '1981-09-26', '202 Cedar St, City, Country');
