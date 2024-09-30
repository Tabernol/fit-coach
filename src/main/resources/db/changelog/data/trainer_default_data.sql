--liquibase formatted sql

--changeset krasnopolskyi:1
INSERT INTO user (first_name, last_name, username, password, is_active)
VALUES ('Arnold', 'Schwarzenegger', 'arnold_schwarzenegger', 'root', TRUE),
       ('Jillian', 'Michaels', 'jillian_michaels', 'root', TRUE),
       ('Rich', 'Froning', 'rich_froning', 'root', TRUE),
       ('Kayla', 'Itsines', 'kayla_itsines', 'root', TRUE),
       ('Shaun', 'T', 'shaun_t', 'root', TRUE),
       ('Tia-Clair', 'Toomey', 'tia_clair_toomey', 'root', TRUE),
       ('Chris', 'Heria', 'chris_heria', 'root', TRUE),
       ('Simone', 'Biles', 'simone_biles', 'root', TRUE),
       ('Ronnie', 'Coleman', 'ronnie_coleman', 'root', TRUE),
       ('Dwayne', 'Johnson', 'dwayne_johnson', 'root', TRUE);

--changeset krasnopolskyi:2
INSERT INTO trainer (user_id, specialization_id)
VALUES ((SELECT id FROM user WHERE username = 'arnold_schwarzenegger'),
        (SELECT id FROM training_type WHERE training_type_name = 'Bodybuilding')),

       ((SELECT id FROM user WHERE username = 'jillian_michaels'),
        (SELECT id FROM training_type WHERE training_type_name = 'Weight Loss')),

       ((SELECT id FROM user WHERE username = 'rich_froning'),
        (SELECT id FROM training_type WHERE training_type_name = 'CrossFit')),

       ((SELECT id FROM user WHERE username = 'kayla_itsines'),
        (SELECT id FROM training_type WHERE training_type_name = 'HIIT')),

       ((SELECT id FROM user WHERE username = 'shaun_t'),
        (SELECT id FROM training_type WHERE training_type_name = 'Dance')),

       ((SELECT id FROM user WHERE username = 'tia_clair_toomey'),
        (SELECT id FROM training_type WHERE training_type_name = 'Functional Fitness')),

       ((SELECT id FROM user WHERE username = 'chris_heria'),
        (SELECT id FROM training_type WHERE training_type_name = 'Resistance')),

       ((SELECT id FROM user WHERE username = 'simone_biles'),
        (SELECT id FROM training_type WHERE training_type_name = 'Stretching')),

       ((SELECT id FROM user WHERE username = 'ronnie_coleman'),
        (SELECT id FROM training_type WHERE training_type_name = 'Powerlifting')),

       ((SELECT id FROM user WHERE username = 'dwayne_johnson'),
        (SELECT id FROM training_type WHERE training_type_name = 'Rehabilitation'));
