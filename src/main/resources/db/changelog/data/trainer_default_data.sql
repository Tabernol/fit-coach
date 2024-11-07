--liquibase formatted sql

--changeset krasnopolskyi:1
INSERT INTO user (first_name, last_name, username, password, is_active)
VALUES ('Arnold', 'Schwarzenegger', 'arnold.schwarzenegger', 'root', TRUE),
       ('Usain', 'Bolt', 'usain.bolt', 'root', TRUE),
       ('Jillian', 'Michaels', 'jillian.michaels', 'root', TRUE),
       ('Rich', 'Froning', 'rich.froning', 'root', TRUE),
       ('Kayla', 'Itsines', 'kayla.itsines', 'root', TRUE);

--changeset krasnopolskyi:2
INSERT INTO trainer (user_id, specialization_id)
VALUES ((SELECT id FROM user WHERE username = 'arnold.schwarzenegger'),
        (SELECT id FROM training_type WHERE training_type_name = 'Bodybuilding')),

       ((SELECT id FROM user WHERE username = 'usain.bolt'),
        (SELECT id FROM training_type WHERE training_type_name = 'Cardio')),

       ((SELECT id FROM user WHERE username = 'jillian.michaels'),
        (SELECT id FROM training_type WHERE training_type_name = 'Weight Loss')),

       ((SELECT id FROM user WHERE username = 'rich.froning'),
        (SELECT id FROM training_type WHERE training_type_name = 'CrossFit')),

       ((SELECT id FROM user WHERE username = 'kayla.itsines'),
        (SELECT id FROM training_type WHERE training_type_name = 'HIIT'));


