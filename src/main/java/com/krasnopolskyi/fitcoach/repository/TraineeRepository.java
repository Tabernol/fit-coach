package com.krasnopolskyi.fitcoach.repository;

import com.krasnopolskyi.fitcoach.entity.Trainee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TraineeRepository extends JpaRepository<Trainee, Long> {
}
