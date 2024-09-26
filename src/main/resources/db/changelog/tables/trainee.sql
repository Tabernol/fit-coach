--liquibase formatted sql

--changeset krasnopolskyi:1
CREATE TABLE IF NOT EXISTS trainee (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    date_of_birth DATE,
    address VARCHAR(256),

    FOREIGN KEY (user_id) REFERENCES user(id)
);
