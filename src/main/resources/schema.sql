CREATE TABLE users (
                                    id INT AUTO_INCREMENT PRIMARY KEY,
                                    first_name VARCHAR(64) NOT NULL,
                                    last_name VARCHAR(64) NOT NULL,
                                    username VARCHAR(64) NOT NULL UNIQUE,
                                    password VARCHAR(256) NOT NULL,
                                    is_active BOOLEAN NOT NULL
);

CREATE TABLE training_type (
                                             id INT AUTO_INCREMENT PRIMARY KEY,
                                             training_type_name VARCHAR(64) NOT NULL UNIQUE
);

CREATE TABLE trainee (
                                       id INT AUTO_INCREMENT PRIMARY KEY,
                                       user_id INT NOT NULL UNIQUE,
                                       date_of_birth DATE,
                                       address VARCHAR(256),

                                       FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE trainer (
                                       id INT AUTO_INCREMENT PRIMARY KEY,
                                       user_id INT NOT NULL UNIQUE,
                                       specialization_id INT NOT NULL,

                                       FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
                                       FOREIGN KEY (specialization_id) REFERENCES training_type(id)
);

CREATE TABLE training (
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
