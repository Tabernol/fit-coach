package com.krasnopolskyi.fitcoach.repository;

import com.krasnopolskyi.fitcoach.entity.Training;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrainingRepository extends JpaRepository<Training, Long> {
}
