--liquibase formatted sql

--changeset krasnopolskyi:1
CREATE TABLE IF NOT EXISTS training (
    id INT AUTO_INCREMENT PRIMARY KEY,
    trainee_id INT NOT NULL,
    trainer_id INT NOT NULL,
    training_name VARCHAR(128) NOT NULL,
    training_type_id INT NOT NULL,
    training_date DATE NOT NULL,
    training_duration INT NOT NULL,

    FOREIGN KEY (trainee_id) REFERENCES trainee(id),
    FOREIGN KEY (trainer_id) REFERENCES trainer(id),
    FOREIGN KEY (training_type_id) REFERENCES training_type(id)
);
