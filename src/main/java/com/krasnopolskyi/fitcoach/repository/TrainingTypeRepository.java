package com.krasnopolskyi.fitcoach.repository;

import com.krasnopolskyi.fitcoach.entity.TrainingType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrainingTypeRepository extends JpaRepository<TrainingType, Integer> {
}
