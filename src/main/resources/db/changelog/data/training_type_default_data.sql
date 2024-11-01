--liquibase formatted sql

--changeset krasnopolskyi:1
INSERT INTO training_type (training_type_name) VALUES
    ('Aerobics'),
    ('Bodybuilding'),
    ('Cardio'),
    ('CrossFit'),
    ('Dance'),
    ('Fitness'),
    ('Functional Fitness'),
    ('HIIT'),
    ('Pilates'),
    ('Powerlifting'),
    ('Rehabilitation'),
    ('Resistance'),
    ('Stretching'),
    ('Weight Loss'),
    ('Yoga'),
    ('Zumba');
