--liquibase formatted sql

--changeset krasnopolskyi:1
CREATE TABLE IF NOT EXISTS trainer (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL UNIQUE,
    specialization_id INT NOT NULL,

    FOREIGN KEY (user_id) REFERENCES user(id) ON DELETE CASCADE,
    FOREIGN KEY (specialization_id) REFERENCES training_type(id)
);
