-- Insert training types
INSERT INTO training_type (training_type_name) VALUES
    ('Bodybuilding'),
    ('Kung fu'),
    ('Cardio'),
    ('HIIT'),
    ('Dance');
-- Insert users (trainees)
INSERT INTO users (first_name, last_name, username, password, is_active)
VALUES
    ('John', 'Doe', 'john.doe', 'root', TRUE),
    ('Jane', 'Smith', 'jane.smith', 'root', TRUE),
    ('Mike', 'Tyson', 'mike.tyson', 'root', TRUE),
    ('Serena', 'Williams', 'serena.williams', 'root', TRUE);

-- Insert trainees
INSERT INTO trainee (user_id, date_of_birth, address)
VALUES
    (1, '1990-05-15', '123 Main St, City, Country'),
    (2, '1985-08-22', '456 Oak St, City, Country'),
    (3, '1966-06-30', '789 Pine St, City, Country'),
    (4, '1981-09-26', '202 Cedar St, City, Country');

-- Insert users (trainers)
INSERT INTO users (first_name, last_name, username, password, is_active)
VALUES
    ('Arnold', 'Schwarzenegger', 'arnold.schwarzenegger', 'root', TRUE),
    ('Bruce', 'Lee', 'bruce.lee', 'root', TRUE),
    ('Usain', 'Bolt', 'usain.bolt', 'root', TRUE),
    ('Kayla', 'Itsines', 'kayla.itsines', 'root', TRUE);

-- Insert trainers
INSERT INTO trainer (user_id, specialization_id)
VALUES
    (5, 1), -- Arnold Schwarzenegger (Bodybuilding)
    (6, 2), -- Bruce Lee (Kung fu)
    (7, 3), -- Usain Bolt (Cardio)
    (8, 4); -- Kayla Itsines (HIIT)
