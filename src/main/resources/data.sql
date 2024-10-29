-- Insert training types
INSERT INTO training_type (training_type_name) VALUES
    ('Bodybuilding'),
    ('Weight Loss'),
    ('CrossFit'),
    ('HIIT'),
    ('Dance');
-- Insert users (trainees)
INSERT INTO users (first_name, last_name, username, password, is_active)
VALUES
    ('John', 'Doe', 'john.doe', 'root', TRUE),
    ('Jane', 'Smith', 'jane.smith', 'root', TRUE),
    ('Mike', 'Tyson', 'mike.tyson', 'root', TRUE),
    ('Usain', 'Bolt', 'usain.bolt', 'root', TRUE),
    ('Serena', 'Williams', 'serena.williams', 'root', TRUE);

-- Insert trainees
INSERT INTO trainee (user_id, date_of_birth, address)
VALUES
    (1, '1990-05-15', '123 Main St, City, Country'),
    (2, '1985-08-22', '456 Oak St, City, Country'),
    (3, '1966-06-30', '789 Pine St, City, Country'),
    (4, '1986-08-21', '101 Maple St, City, Country'),
    (5, '1981-09-26', '202 Cedar St, City, Country');

-- Insert users (trainers)
INSERT INTO users (first_name, last_name, username, password, is_active)
VALUES
    ('Arnold', 'Schwarzenegger', 'arnold.schwarzenegger', 'root', TRUE),
    ('Jillian', 'Michaels', 'jillian.michaels', 'root', TRUE),
    ('Rich', 'Froning', 'rich.froning', 'root', TRUE),
    ('Kayla', 'Itsines', 'kayla.itsines', 'root', TRUE);

-- Insert trainers
INSERT INTO trainer (user_id, specialization_id)
VALUES
    (6, 1), -- Arnold Schwarzenegger (Bodybuilding)
    (7, 2), -- Jillian Michaels (Weight Loss)
    (8, 3), -- Rich Froning (CrossFit)
    (9, 4); -- Kayla Itsines (HIIT)
