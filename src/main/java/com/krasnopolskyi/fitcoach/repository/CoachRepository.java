package com.krasnopolskyi.fitcoach.repository;

import com.krasnopolskyi.fitcoach.entity.Coach;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CoachRepository extends JpaRepository<Coach, Long> {
}
